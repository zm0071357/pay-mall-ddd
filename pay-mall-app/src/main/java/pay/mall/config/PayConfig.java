package pay.mall.config;

import ltzf.factory.PayFactory;
import ltzf.factory.defaults.DefaultPayFactory;
import ltzf.payments.h5.impl.H5PayServiceImpl;
import ltzf.payments.jsapi.impl.JSServiceImpl;
import ltzf.payments.jump_h5.impl.JumpH5ServiceImpl;
import ltzf.payments.nativepay.impl.NativePayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付服务动态配置入口
 */
@Configuration
@EnableConfigurationProperties(PayConfigProperties.class)
public class PayConfig {

    private final Logger logger = LoggerFactory.getLogger(PayConfig.class);

    /**
     * 支付服务工厂
     * @param properties
     * @return
     */
    @Bean(name = "payFactory")
    public PayFactory payFactory(PayConfigProperties properties) {
        ltzf.factory.Configuration configuration = new ltzf.factory.Configuration(
                properties.getAppId(), properties.getMchId(), properties.getPartnerKey());
        logger.info("蓝兔支付配置完成");
        return new DefaultPayFactory(configuration);
    }

    /**
     * 扫码支付服务
     * @param payFactory
     * @return
     */
    @Bean(name = "nativePayService")
    // 是否启动该服务
    @ConditionalOnProperty(value = "ltzf.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public NativePayServiceImpl nativePayService(PayFactory payFactory) {
        logger.info("扫码支付服务装配完成");
        return payFactory.nativePayService();
    }

    /**
     * H5支付服务
     * @param payFactory
     * @return
     */
    @Bean(name = "h5PayService")
    @ConditionalOnProperty(value = "ltzf.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public H5PayServiceImpl h5PayService(PayFactory payFactory) {
        logger.info("H5支付服务装配完成");
        return payFactory.h5PayService();
    }

    /**
     * H5[跳转模式]支付服务
     * @param payFactory
     * @return
     */
    @Bean(name = "jumpH5PayService")
    @ConditionalOnProperty(value = "ltzf.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public JumpH5ServiceImpl jumpH5PayService(PayFactory payFactory) {
        logger.info("H5[跳转模式]支付服务装配完成");
        return payFactory.jumpH5Service();
    }

    /**
     * 公众号支付服务
     * @param payFactory
     * @return
     */
    @Bean(name = "jsPayService")
    @ConditionalOnProperty(value = "ltzf.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public JSServiceImpl jsPayService(PayFactory payFactory) {
        logger.info("公众号支付服务装配完成");
        return payFactory.jsService();
    }



}
