package pay.mall.api.dto;

import lombok.Data;

/**
 * 支付请求对象
 */
@Data
public class CreatePayRequestDTO {

    /**
     * 用户ID - account
     */
    private String userId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 拼团ID
     */
    private String teamId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 营销类型 - 0无营销
     */
    private Integer marketType = 0;

}
