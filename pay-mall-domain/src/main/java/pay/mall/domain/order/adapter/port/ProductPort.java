package pay.mall.domain.order.adapter.port;

import pay.mall.domain.order.model.entity.MarketPayDiscountEntity;
import pay.mall.domain.order.model.entity.ProductEntity;

import java.util.Date;


/**
 * rpc调用
 * 一般商品和支付会抽成两个微服务
 * 由支付去调商品的微服务
 */
public interface ProductPort {

    /**
     * 查询商品
     * @param productId
     * @return
     */
    ProductEntity queryProductByProductId(String productId);

    /**
     * 营销锁单
     * @param userId
     * @param teamId
     * @param activityId
     * @param productId
     * @param orderId
     * @return
     */
    MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId);

    /**
     * 营销结算
     * @param userId
     * @param orderId
     * @param orderTime
     */
    void settlementMarketPayOrder(String userId, String orderId, Date orderTime);
}
