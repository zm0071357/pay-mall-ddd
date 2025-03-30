package pay.mall.trigger.job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pay.mall.domain.order.service.OrderService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超时关单任务
 */
@Slf4j
@Component
public class TimeoutCloseOrderJob {
    @Resource
    private OrderService orderService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void exec() {
        try {
            log.info("任务；超时10分钟订单关闭");
            List<String> orderIds = orderService.queryTimeoutCloseOrderList();
            if (null == orderIds || orderIds.isEmpty()) {
                log.info("定时任务，超时10分钟订单关闭，暂无超时未支付订单 orderIds is null");
                return;
            }
            for (String orderId : orderIds) {
                boolean status = orderService.changeOrderClose(orderId);
                log.info("定时任务，超时10分钟订单关闭 orderId: {} status：{}", orderId, status);
            }
        } catch (Exception e) {
            log.error("定时任务，超时10分钟订单关闭失败", e);
        }
    }
}
