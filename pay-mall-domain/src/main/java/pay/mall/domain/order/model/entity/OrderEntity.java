package pay.mall.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pay.mall.domain.order.model.valobj.OrderStatusVO;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单创建时间
     */
    private Date orderTime;

    /**
     * 订单总价格
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private OrderStatusVO orderStatusVO;

    /**
     * 支付地址
     */
    private String payUrl;

    /**
     * 营销类型：0 无营销、1 拼团营销
     */
    private Integer marketType;

    /**
     * 优惠金额
     */
    private BigDecimal marketDeductionAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;


}
