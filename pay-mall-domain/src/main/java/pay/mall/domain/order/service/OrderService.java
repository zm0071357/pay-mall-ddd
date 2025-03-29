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


}
