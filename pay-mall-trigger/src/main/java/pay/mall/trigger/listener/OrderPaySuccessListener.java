package pay.mall.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
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

    @Subscribe
    public void handleEvent(String paySuccessMessageJSON) {
        log.info("收到支付成功消息 {}", paySuccessMessageJSON);
        PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = JSON.parseObject(paySuccessMessageJSON, PaySuccessMessageEvent.PaySuccessMessage.class);
        log.info("商品已在打包发货中，单号:{}", paySuccessMessage.getTradeNo());
        goodsService.changeOrderDealDone(paySuccessMessage.getTradeNo());
    }

}
