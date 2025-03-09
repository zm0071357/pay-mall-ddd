package pay.mall.domain.order.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import ltzf.factory.Configuration;
import ltzf.factory.PayFactory;
import ltzf.factory.defaults.DefaultPayFactory;
import ltzf.payments.nativepay.impl.NativePayServiceImpl;
import ltzf.payments.nativepay.model.prepay.PrepayRequest;
import ltzf.payments.nativepay.model.prepay.PrepayResponse;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pay.mall.domain.order.adapter.port.ProductPort;
import pay.mall.domain.order.adapter.repository.OrderRepository;
import pay.mall.domain.order.model.aggregate.CreateOrderAggregate;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.valobj.OrderStatusVO;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl extends AbstractOrderService {

    @Value("${ltzf.sdk.config.notify_url}")
    private String notifyUrl;

    @Value("${ltzf.sdk.config.mch_id}")
    private String mchId;

    @Resource
    private NativePayServiceImpl nativePayServiceImpl;

    @Resource
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductPort productPort) {
        super(orderRepository, productPort);
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate orderAggregate) {
        orderRepository.doSaveOrder(orderAggregate);
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws IOException {
        // 封装请求
        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setMchId(mchId);
        prepayRequest.setOutTradeNo(orderId);
        prepayRequest.setTotalFee(totalAmount.toString());
        prepayRequest.setBody(productName);
        prepayRequest.setNotifyUrl(notifyUrl);
        log.info("请求参数:{}", JSON.toJSONString(prepayRequest));

        // 预支付
        PrepayResponse prepayResponse = nativePayServiceImpl.prePay(prepayRequest);
        log.info("返回结果:{}", JSON.toJSONString(prepayResponse));
        String qrcodeUrl = prepayResponse.getData().getQrcodeUrl();

        // 封装返回结果
        PayOrderEntity payOrderEntity = new PayOrderEntity();
        payOrderEntity.setOrderId(orderId);
        payOrderEntity.setOrderStatus(OrderStatusVO.PAY_WAIT);
        payOrderEntity.setPayUrl(qrcodeUrl);

        // 更新
        orderRepository.updateOrderPayInfo(payOrderEntity);

        return payOrderEntity;
    }

    @Override
    public void changeOrderPaySuccess(String orderId) {
        orderRepository.changeOrderPaySuccess(orderId);
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderRepository.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderRepository.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderRepository.changeOrderClose(orderId);
    }
}
