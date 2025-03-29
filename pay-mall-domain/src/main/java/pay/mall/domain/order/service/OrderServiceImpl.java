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
import pay.mall.domain.order.model.entity.MarketPayDiscountEntity;
import pay.mall.domain.order.model.entity.OrderEntity;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.valobj.MarketTypeVO;
import pay.mall.domain.order.model.valobj.OrderStatusVO;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
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
    protected MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId) {
        return productPort.lockMarketPayOrder(userId, teamId, activityId, productId, orderId);
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate orderAggregate) {
        orderRepository.doSaveOrder(orderAggregate);
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws IOException {
        return doPrepayOrder(userId, productId, productName, orderId, totalAmount, null);
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount, MarketPayDiscountEntity marketPayDiscountEntity) throws IOException {
        // 最终支付金额
        BigDecimal payAmount = marketPayDiscountEntity == null ? totalAmount : marketPayDiscountEntity.getPayPrice();

        // 封装请求
        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setMchId(mchId);
        prepayRequest.setOutTradeNo(orderId);
        prepayRequest.setTotalFee(payAmount.toString());
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

        // 营销信息
        payOrderEntity.setMarketType(null == marketPayDiscountEntity ? MarketTypeVO.NO_MARKET.getCode() : MarketTypeVO.GROUP_BUY_MARKET.getCode());
        payOrderEntity.setMarketDeductionAmount(null == marketPayDiscountEntity ? BigDecimal.ZERO : marketPayDiscountEntity.getDeductionPrice());
        payOrderEntity.setPayAmount(payAmount);

        // 更新
        orderRepository.updateOrderPayInfo(payOrderEntity);

        return payOrderEntity;
    }

    @Override
    public void changeOrderPaySuccess(String orderId, Date payTime) {
        // 查询订单
        OrderEntity orderEntity = orderRepository.queryOrderByOrderId(orderId);
        if (orderEntity == null) {
            return;
        }
        // 拼团营销
        if (MarketTypeVO.GROUP_BUY_MARKET.getCode().equals(orderEntity.getMarketType())) {
            // 更新订单状态为支付成功
            orderRepository.changeMarketOrderPaySuccess(orderId);
            // 营销结算
            productPort.settlementMarketPayOrder(orderEntity.getUserId(), orderId, payTime);
        }
        // 无营销
        else {
            // 更新订单状态为支付成功
            orderRepository.changeOrderPaySuccess(orderId, payTime);
        }
    }

    @Override
    public void changeOrderMarketSettlement(List<String> outTradeNoList) {
        orderRepository.changeOrderMarketSettlement(outTradeNoList);
    }
}
