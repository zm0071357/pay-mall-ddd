package pay.mall.trigger.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pay.mall.api.dto.NotifyRequestDTO;
import pay.mall.domain.order.service.OrderService;

import javax.annotation.Resource;

/**
 * 拼团完成消费消息监听器
 */
@Slf4j
@Component
public class TeamSuccessTopicListener {

    @Resource
    private OrderService orderService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.consumer.topic_team_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.consumer.topic_team_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_team_success.routing_key}"
            )
    )
    public void listener(String message) {
        try {
            NotifyRequestDTO requestDTO = JSON.parseObject(message, NotifyRequestDTO.class);
            log.info("拼团回调，组队完成，结算开始 {}", JSON.toJSONString(requestDTO));
            // 营销结算
            orderService.changeOrderMarketSettlement(requestDTO.getOutTradeNoList());
        } catch (Exception e) {
            log.error("拼团回调，组队完成，结算失败 {}", message, e);
            throw e;
        }
    }
}
