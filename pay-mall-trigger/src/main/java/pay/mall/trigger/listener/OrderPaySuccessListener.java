package pay.mall.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pay.mall.domain.goods.service.GoodsService;
import pay.mall.domain.order.adapter.event.PaySuccessMessageEvent;

import javax.annotation.Resource;

/**
 * 支付成功回调消息监听
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private GoodsService goodsService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.consumer.topic_order_pay_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.consumer.topic_order_pay_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_order_pay_success.routing_key}"
            )
    )
    public void listener(String paySuccessMessageJson) {
        try {
            log.info("收到支付成功消息 {}", paySuccessMessageJson);
            PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = JSON.parseObject(paySuccessMessageJson, PaySuccessMessageEvent.PaySuccessMessage.class);
            log.info("模拟发货（如；发货、充值、开户员、返利），单号:{}", paySuccessMessage.getTradeNo());
            // 变更订单状态 - 发货完成&结算
            goodsService.changeOrderDealDone(paySuccessMessage.getTradeNo());

            // MQ 消费失败会抛异常，之后重试消费。这个也是最终执行的重要手段。
            // throw new RuntimeException("重试消费");
        } catch (Exception e) {
            log.error("收到支付成功消息失败 {}", paySuccessMessageJson,e);
            throw e;
        }
    }


}
