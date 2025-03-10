package pay.mall.trigger.job;

import lombok.extern.slf4j.Slf4j;
import ltzf.payments.nativepay.NativePayService;
import ltzf.payments.nativepay.impl.NativePayServiceImpl;
import ltzf.payments.nativepay.model.order.OrderRequest;
import ltzf.payments.nativepay.model.order.OrderResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pay.mall.domain.order.service.OrderService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 检测未接收到或未正确处理的支付回调通知任务
 */
@Slf4j
@Component
public class OrderNoPayNotifyJob {

    @Resource
    private OrderService orderService;

    @Resource
    private NativePayServiceImpl nativePayServiceImpl;

    @Value("${ltzf.sdk.config.mch_id}")
    private String mchId;

    @Value("${ltzf.sdk.config.partner_key}")
    private String partnerKey;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("任务；检测未接收到或未正确处理的支付回调通知");
            List<String> orderIds = orderService.queryNoPayNotifyOrder();
            if (null == orderIds || orderIds.isEmpty()) return;

            for (String orderId : orderIds) {
                // 查询订单
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setMchId(mchId);
                orderRequest.setOutTradeNo(orderId);
                orderRequest.setSign(orderRequest.createSign(partnerKey));
                OrderResponse order = nativePayServiceImpl.getOrder(orderRequest);
                Long code = order.getCode();
                if (order.getData() != null) {
                    Integer payStatus = order.getData().getPayStatus();
                    // 订单状态为1 - 已支付，更新数据库订单状态为支付成功
                    if (code == 0 && payStatus == 1) {
                        orderService.changeOrderPaySuccess(orderId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("检测未接收到或未正确处理的支付回调通知失败", e);
        }
    }


}
