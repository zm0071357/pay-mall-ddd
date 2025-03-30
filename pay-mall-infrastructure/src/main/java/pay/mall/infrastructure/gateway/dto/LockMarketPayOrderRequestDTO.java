package pay.mall.infrastructure.gateway.dto;

import lombok.Data;

/**
 * 营销支付锁单请求对象
 */
@Data
public class LockMarketPayOrderRequestDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 拼单组队ID - 可为空，为空则创建新组队ID
     */
    private String teamId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 商品ID
     */
    private String goodsId;

    /**
     * 渠道
     */
    private String source;

    /**
     * 来源
     */
    private String channel;

    /**
     * 外部交易单号
     */
    private String outTradeNo;

    /**
     * 回调配置
     */
    private NotifyConfigVO notifyConfigVO;


    // 兼容配置
    public void setNotifyUrl(String url) {
        NotifyConfigVO notifyConfigVO = new NotifyConfigVO();
        notifyConfigVO.setNotifyType("HTTP");
        notifyConfigVO.setNotifyUrl(url);
        this.notifyConfigVO = notifyConfigVO;
    }

    // 兼容配置 - MQ不需要指定，系统会发统一MQ消息
    public void setNotifyMQ() {
        NotifyConfigVO notifyConfigVO = new NotifyConfigVO();
        notifyConfigVO.setNotifyType("MQ");
        this.notifyConfigVO = notifyConfigVO;
    }

    @Data
    public static class NotifyConfigVO {

        /**
         * 回调方式；MQ、HTTP
         */
        private String notifyType;

        /**
         * MQ回调主题 - MQ配置
         */
        private String notifyMQ;

        /**
         * 回调地址 - HTTP配置
         */
        private String notifyUrl;
    }




}

