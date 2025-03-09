package pay.mall.domain.order.service;

import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;

import java.util.List;

public interface OrderService {

    /**
     * 创建订单
     * @param shopCartEntity
     * @return
     * @throws Exception
     */
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception;

    /**
     * 支付完成
     * @param orderId
     */
    void changeOrderPaySuccess(String orderId);

    /**
     * 查询没有回调的订单
     * @return
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时关单的订单
     * @return
     */
    List<String> queryTimeoutCloseOrderList();

    /**
     * 关单
     * @param orderId
     * @return
     */
    boolean changeOrderClose(String orderId);
}
