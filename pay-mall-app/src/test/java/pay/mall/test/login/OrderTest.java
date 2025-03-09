package pay.mall.test.login;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import ltzf.payments.nativepay.impl.NativePayServiceImpl;
import ltzf.payments.nativepay.model.prepay.PrepayRequest;
import ltzf.payments.nativepay.model.prepay.PrepayResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;
import pay.mall.domain.order.service.OrderService;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderTest {

    @Resource
    private OrderService orderService;

    @Resource
    private NativePayServiceImpl nativePayServiceImpl;

    @Test
    public void test_createOrder() throws Exception {
        ShopCartEntity shopCartEntity = new ShopCartEntity();
        shopCartEntity.setUserId("zm0031357");
        shopCartEntity.setProductId("10001");
        PayOrderEntity payOrderEntity = orderService.createOrder(shopCartEntity);
        log.info("请求参数:{}", JSON.toJSONString(shopCartEntity));
        log.info("测试结果:{}", JSON.toJSONString(payOrderEntity));
    }

    @Test
    public void test_nativePay() throws Exception {
        PrepayRequest request = new PrepayRequest();
        request.setMchId("1698731613");
        request.setOutTradeNo("PDD20280121");
        request.setTotalFee("0.01");
        request.setBody("悠悠球");
        request.setNotifyUrl("https://xiaoming-programming.top");

        PrepayResponse response = nativePayServiceImpl.prePay(request);
        log.info("请求参数:{}", JSON.toJSONString(request));
        log.info("返回结果:{}", JSON.toJSONString(response));
    }

}
