package pay.mall.domain.order.adapter.repository;

import pay.mall.domain.order.model.aggregate.CreateOrderAggregate;
import pay.mall.domain.order.model.entity.OrderEntity;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;

import java.util.Date;
import java.util.List;

public interface OrderRepository {

    /**
     * 保存订单
     * @param orderAggregate
     */
    void doSaveOrder(CreateOrderAggregate orderAggregate);

    /**
     * 获取未支付订单
     * @param shopCartEntity
     * @return
     */
    OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity);

    /**
     * 更新订单信息
     * @param payOrderEntity
     */
    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    /**
     * 支付完成
     * @param orderId
     * @param orderTime
     */
    void changeOrderPaySuccess(String orderId, Date orderTime);

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

    /**
     * 根据订单ID查询订单
     * @param orderId
     * @return
     */
    OrderEntity queryOrderByOrderId(String orderId);

    /**
     * 拼团 - 支付完成
     * @param orderId
     */
    void changeMarketOrderPaySuccess(String orderId);

    /**
     * 拼团完成
     * @param outTradeNoList
     */
    void changeOrderMarketSettlement(List<String> outTradeNoList);
}
