package pay.mall.domain.order.service;

import lombok.extern.slf4j.Slf4j;
import pay.mall.domain.order.adapter.port.ProductPort;
import pay.mall.domain.order.adapter.repository.OrderRepository;
import pay.mall.domain.order.model.aggregate.CreateOrderAggregate;
import pay.mall.domain.order.model.entity.OrderEntity;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ProductEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;
import pay.mall.domain.order.model.valobj.OrderStatusVO;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 抽象类
 * 定义一些通用的逻辑或业务流程（例如订单检查、验证等）
 * 如果需要添加新的订单处理方式，只需要新增一个继承 AbstractOrderService 的类并实现具体的 doSaveOrder 逻辑，而不需要去修改 AbstractOrderService
 */
@Slf4j
public abstract class AbstractOrderService implements OrderService {

    protected final OrderRepository orderRepository;

    protected final ProductPort productPort;

    public AbstractOrderService(OrderRepository orderRepository, ProductPort productPort) {
        this.orderRepository = orderRepository;
        this.productPort = productPort;
    }

    @Override
    public PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception {
        // 查询当前用户是否存在掉单和未支付订单
        OrderEntity unpaidOrderEntity = orderRepository.queryUnPayOrder(shopCartEntity);
        if (null != unpaidOrderEntity && OrderStatusVO.PAY_WAIT.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("创建订单-存在，已存在未支付订单。userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
            return PayOrderEntity.builder()
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .build();
        } else if (null != unpaidOrderEntity && OrderStatusVO.CREATE.equals(unpaidOrderEntity.getOrderStatusVO())) {
            // 支付
            log.info("创建订单-存在，存在未创建支付单订单，创建支付单开始 userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
            PayOrderEntity payOrderEntity = doPrepayOrder(shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getTotalAmount());
            return PayOrderEntity.builder()
                    .orderId(payOrderEntity.getOrderId())
                    .payUrl(payOrderEntity.getPayUrl())
                    .build();
        }
        // 正常创建订单
        log.info("创建订单-不存在，正常创建订单。创建订单开始 userId:{} productId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId());
        // 查询商品信息
        ProductEntity productEntity = productPort.queryProductByProductId(shopCartEntity.getProductId());
        OrderEntity orderEntity = CreateOrderAggregate.buildOrderEntity(productEntity.getProductId(), productEntity.getProductName());
        CreateOrderAggregate orderAggregate = CreateOrderAggregate.builder()
                .userId(shopCartEntity.getUserId())
                .productEntity(productEntity)
                .orderEntity(orderEntity)
                .build();
        // 保存订单
        this.doSaveOrder(orderAggregate);
        PayOrderEntity payOrderEntity = doPrepayOrder(shopCartEntity.getUserId(), shopCartEntity.getProductId(), productEntity.getProductName(), orderEntity.getOrderId(), productEntity.getPrice());
        log.info("创建订单-完成，生成支付单。userId: {} orderId: {} payUrl: {}", shopCartEntity.getUserId(), orderEntity.getOrderId(), payOrderEntity.getPayUrl());
        return PayOrderEntity.builder()
                .orderId(orderEntity.getOrderId())
                .payUrl(payOrderEntity.getPayUrl())
                .build();
    }


    /**
     * 插入订单
     * @param orderAggregate
     */
    protected abstract void doSaveOrder(CreateOrderAggregate orderAggregate);

    /**
     * 预支付
     * @param userId
     * @param productId
     * @param productName
     * @param orderId
     * @param totalAmount
     * @return
     */
    protected abstract PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws IOException;

}
