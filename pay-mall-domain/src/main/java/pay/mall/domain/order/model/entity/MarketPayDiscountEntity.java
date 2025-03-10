package pay.mall.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPayDiscountEntity {

    /**
     * 原始金额
     */
    private BigDecimal originalPrice;

    /**
     * 折扣金额
     */
    private BigDecimal deductionPrice;

    /**
     * 支付金额
     */
    private BigDecimal payPrice;
}
