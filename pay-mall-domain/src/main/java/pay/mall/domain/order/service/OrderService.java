package pay.mall.domain.order.service;

import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;

import java.io.IOException;
import java.util.Date;
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
     * 订单支付成功
     * @param orderId
     * @param payTime
     */
    void changeOrderPaySuccess(String orderId, Date payTime);


    /**
     * 拼团回调 - 通知商城系统已经结算完成，进行发货等操作
     * @param outTradeNoList
     */
    void changeOrderMarketSettlement(List<String> outTradeNoList);


    /**
     * 查询没有回调的订单集合
     * @return
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时的订单集合
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
