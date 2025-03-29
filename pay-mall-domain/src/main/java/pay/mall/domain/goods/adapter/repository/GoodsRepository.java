package pay.mall.domain.goods.adapter.repository;

/**
 * 商品仓储
 */
public interface GoodsRepository {

    /**
     * 更新订单为完成状态
     * @param tradeNo
     */
    void changeOrderDealDone(String tradeNo);
}
