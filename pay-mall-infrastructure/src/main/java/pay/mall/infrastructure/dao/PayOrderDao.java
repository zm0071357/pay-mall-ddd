package pay.mall.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pay.mall.domain.order.model.entity.OrderEntity;
import pay.mall.infrastructure.dao.po.PayOrder;

import java.util.List;

@Mapper
public interface PayOrderDao {

    /**
     * 插入订单
     * @param payOrder
     */
    void insert(PayOrder payOrder);

    /**
     * 查询未支付订单
     * @param payOrder
     * @return
     */
    PayOrder queryUnPayOrder(PayOrder payOrder);

    /**
     * 更新订单信息
     * @param payOrderReq
     */
    void updateOrderPayInfo(PayOrder payOrderReq);

    /**
     * 支付完成
     * @param payOrderReq
     */
    void changeOrderPaySuccess(PayOrder payOrderReq);

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
    PayOrder queryOrderByOrderId(String orderId);

    /**
     * 拼团完成
     * @param outTradeNoList
     */
    void changeOrderMarketSettlement(@Param("outTradeNoList") List<String> outTradeNoList);


    void changeOrderDealDone(String tradeNo);
}
