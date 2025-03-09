package pay.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 支付服务配置
 */
@Data
@ConfigurationProperties(prefix = "ltzf.sdk.config", ignoreInvalidFields = true)
public class PayConfigProperties {

    /**
     * 服务是否开启
     */
    private boolean enable;

    /**
     * 开发者 Id
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 密钥
     */
    private String partnerKey;

}
