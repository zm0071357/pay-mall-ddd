package pay.mall.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrder {

    /**
     * 自增ID
     */
    private Long id;

    /**
     * 用户ID - account
     */
    private String userId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品名字
     */
    private String productName;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     * create-创建完成
     * pay_wait-等待支付
     * pay_success-支付成功
     * deal_done-交易完成
     * close-订单关单'
     */
    private String status;

    /**
     * 支付地址
     */
    private String payUrl;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
