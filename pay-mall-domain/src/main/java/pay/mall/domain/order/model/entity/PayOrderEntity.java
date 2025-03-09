package pay.mall.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pay.mall.domain.order.model.valobj.OrderStatusVO;

/**
 * 订单实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 支付url
     */
    private String payUrl;

    /**
     * 订单状态
     */
    private OrderStatusVO orderStatus;

}
