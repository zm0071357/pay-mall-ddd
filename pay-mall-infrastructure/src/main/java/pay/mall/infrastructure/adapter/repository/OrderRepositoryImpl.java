package pay.mall.infrastructure.adapter.repository;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pay.mall.domain.order.adapter.event.PaySuccessMessageEvent;
import pay.mall.domain.order.adapter.repository.OrderRepository;
import pay.mall.domain.order.model.aggregate.CreateOrderAggregate;
import pay.mall.domain.order.model.entity.OrderEntity;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ProductEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;
import pay.mall.domain.order.model.valobj.MarketTypeVO;
import pay.mall.domain.order.model.valobj.OrderStatusVO;
import pay.mall.infrastructure.dao.PayOrderDao;
import pay.mall.infrastructure.dao.po.PayOrder;
import pay.mall.types.event.BaseEvent;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderRepositoryImpl implements OrderRepository {

    @Resource
    private PayOrderDao payOrderDao;

    @Resource
    private PaySuccessMessageEvent paySuccessMessageEvent;

    @Resource
    private EventBus eventBus;

    @Override
    public void doSaveOrder(CreateOrderAggregate orderAggregate) {
        String userId = orderAggregate.getUserId();
        ProductEntity productEntity = orderAggregate.getProductEntity();
        OrderEntity orderEntity = orderAggregate.getOrderEntity();

        PayOrder order = new PayOrder();
        order.setUserId(userId);
        order.setProductId(productEntity.getProductId());
        order.setProductName(productEntity.getProductName());
        order.setOrderId(orderEntity.getOrderId());
        order.setOrderTime(orderEntity.getOrderTime());
        order.setTotalAmount(productEntity.getPrice());
        order.setStatus(orderEntity.getOrderStatusVO().getCode());
        order.setMarketType(MarketTypeVO.NO_MARKET.getCode());
        order.setMarketDeductionAmount(BigDecimal.ZERO);
        order.setPayAmount(productEntity.getPrice());
        order.setMarketType(order.getMarketType());

        payOrderDao.insert(order);
    }

    @Override
    public OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity) {
        // 封装参数
        PayOrder orderReq = new PayOrder();
        orderReq.setUserId(shopCartEntity.getUserId());
        orderReq.setProductId(shopCartEntity.getProductId());

        // 查询订单
        PayOrder order = payOrderDao.queryUnPayOrder(orderReq);
        if (null == order) return null;

        // 返回结果
        return OrderEntity.builder()
                .productId(order.getProductId())
                .productName(order.getProductName())
                .orderId(order.getOrderId())
                .orderStatusVO(OrderStatusVO.valueOf(order.getStatus()))
                .orderTime(order.getOrderTime())
                .totalAmount(order.getTotalAmount())
                .payUrl(order.getPayUrl())
                .marketType(order.getMarketType())
                .marketDeductionAmount(order.getMarketDeductionAmount())
                .payAmount(order.getPayAmount())
                .build();
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        PayOrder payOrderReq = PayOrder.builder()
                .userId(payOrderEntity.getUserId())
                .orderId(payOrderEntity.getOrderId())
                .status(payOrderEntity.getOrderStatus().getCode())
                .payUrl(payOrderEntity.getPayUrl())
                .marketType(payOrderEntity.getMarketType())
                .marketDeductionAmount(payOrderEntity.getMarketDeductionAmount())
                .payAmount(payOrderEntity.getPayAmount())
                .build();
        payOrderDao.updateOrderPayInfo(payOrderReq);
    }

    @Override
    public void changeOrderPaySuccess(String orderId, Date orderTime) {
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setStatus(OrderStatusVO.PAY_SUCCESS.getCode());
        // 更新数据库中订单状态 - 支付成功
        payOrderDao.changeOrderPaySuccess(payOrderReq);

        // 发送MQ信息
        BaseEvent.EventMessage<PaySuccessMessageEvent.PaySuccessMessage> paySuccessMessageEventMessage = paySuccessMessageEvent.buildEventMessage(PaySuccessMessageEvent.PaySuccessMessage.builder().tradeNo(orderId).build());
        PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = paySuccessMessageEventMessage.getData();
        eventBus.post(JSON.toJSONString(paySuccessMessage));
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return payOrderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return payOrderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return payOrderDao.changeOrderClose(orderId);
    }

    @Override
    public OrderEntity queryOrderByOrderId(String orderId) {
        PayOrder payOrder = payOrderDao.queryOrderByOrderId(orderId);
        if (payOrder == null) {
            return null;
        }
        return OrderEntity.builder()
                .userId(payOrder.getUserId())
                .productId(payOrder.getProductId())
                .productName(payOrder.getProductName())
                .orderId(payOrder.getOrderId())
                .orderTime(payOrder.getOrderTime())
                .totalAmount(payOrder.getTotalAmount())
                .payUrl(payOrder.getPayUrl())
                .marketType(payOrder.getMarketType())
                .marketDeductionAmount(payOrder.getMarketDeductionAmount())
                .payAmount(payOrder.getPayAmount())
                .build();

    }

    @Override
    public void changeMarketOrderPaySuccess(String orderId) {
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setStatus(OrderStatusVO.PAY_SUCCESS.getCode());
        // 更新数据库中订单状态 - 支付成功
        payOrderDao.changeOrderPaySuccess(payOrderReq);
    }

    @Override
    public void changeOrderMarketSettlement(List<String> outTradeNoList) {
        payOrderDao.changeOrderMarketSettlement(outTradeNoList);
        for (String outTradeNo : outTradeNoList) {
            // 发送MQ信息
            BaseEvent.EventMessage<PaySuccessMessageEvent.PaySuccessMessage> paySuccessMessageEventMessage = paySuccessMessageEvent.buildEventMessage(PaySuccessMessageEvent.PaySuccessMessage.builder().tradeNo(outTradeNo).build());
            PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = paySuccessMessageEventMessage.getData();
            eventBus.post(JSON.toJSONString(paySuccessMessage));
        }
    }
}
