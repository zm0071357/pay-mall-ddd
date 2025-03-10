package pay.mall.domain.order.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 营销类型
 */
@Getter
@AllArgsConstructor
public enum MarketTypeVO {

    NO_MARKET(0, "无营销"),
    GROUP_BUY_MARKET(1, "拼团营销"),
    ;

    private final Integer code;
    private final String desc;

    public static MarketTypeVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return NO_MARKET;
            case 1:
                return GROUP_BUY_MARKET;
        }
        throw new RuntimeException("err code not exist");
    }
}
