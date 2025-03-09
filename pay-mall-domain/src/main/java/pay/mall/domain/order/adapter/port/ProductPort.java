package pay.mall.domain.order.adapter.port;

import pay.mall.domain.order.model.entity.ProductEntity;

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
}
