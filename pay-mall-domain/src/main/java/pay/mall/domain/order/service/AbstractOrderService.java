package pay.mall.domain.order.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import pay.mall.domain.order.adapter.port.ProductPort;
import pay.mall.domain.order.adapter.repository.OrderRepository;
import pay.mall.domain.order.model.aggregate.CreateOrderAggregate;
import pay.mall.domain.order.model.entity.*;
import pay.mall.domain.order.model.valobj.MarketTypeVO;
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
        // 查询当前用户是否存在未支付订单
        OrderEntity unpaidOrderEntity = orderRepository.queryUnPayOrder(shopCartEntity);
        if (null != unpaidOrderEntity && OrderStatusVO.PAY_WAIT.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("创建订单-已存在未支付订单。userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
            return PayOrderEntity.builder()
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .build();
        // 当前用户是否存在掉单
        } else if (null != unpaidOrderEntity && OrderStatusVO.CREATE.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("创建订单-存在未创建支付单的订单，创建支付单 userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
            Integer marketType = unpaidOrderEntity.getMarketType();
            BigDecimal marketDeductionAmount = unpaidOrderEntity.getMarketDeductionAmount();
            PayOrderEntity payOrderEntity = null;
            // 营销锁单出现异常导致未成功抵消价格
            // 重新进行营销锁单
            if (MarketTypeVO.GROUP_BUY_MARKET.getCode().equals(marketType) && null == marketDeductionAmount) {
                log.info("营销锁单出现异常导致未成功抵消价格 - 重新进行营销锁单");
                MarketPayDiscountEntity marketPayDiscountEntity = this.lockMarketPayOrder(shopCartEntity.getUserId(),
                        shopCartEntity.getTeamId(),
                        shopCartEntity.getActivityId(),
                        shopCartEntity.getProductId(),
                        unpaidOrderEntity.getOrderId());

                payOrderEntity = doPrepayOrder(shopCartEntity.getUserId(), shopCartEntity.getProductId(),
                        unpaidOrderEntity.getProductName(), unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getTotalAmount(), marketPayDiscountEntity);
            // 锁单成功
            } else if (MarketTypeVO.GROUP_BUY_MARKET.getCode().equals(marketType)) {
                log.info("拼团营销-重新营销锁单 userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
                MarketPayDiscountEntity marketPayDiscountEntity = this.lockMarketPayOrder(shopCartEntity.getUserId(),
                        shopCartEntity.getTeamId(),
                        shopCartEntity.getActivityId(),
                        shopCartEntity.getProductId(),
                        unpaidOrderEntity.getOrderId());
                payOrderEntity = doPrepayOrder(shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getProductName(),
                        unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayAmount(), marketPayDiscountEntity);
            // 未进行营销
            } else {
                log.info("未进行营销 userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
                payOrderEntity = doPrepayOrder(shopCartEntity.getUserId(), shopCartEntity.getProductId(),
                        unpaidOrderEntity.getProductName(), unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getTotalAmount());
            }
            return PayOrderEntity.builder()
                    .orderId(payOrderEntity.getOrderId())
                    .payUrl(payOrderEntity.getPayUrl())
                    .build();
        }

        // 正常创建订单
        log.info("创建订单-不存在，正常创建订单 userId:{} productId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId());
        // 查询商品信息
        ProductEntity productEntity = productPort.queryProductByProductId(shopCartEntity.getProductId());
        OrderEntity orderEntity = CreateOrderAggregate.buildOrderEntity(productEntity.getProductId(), productEntity.getProductName());
        CreateOrderAggregate orderAggregate = CreateOrderAggregate.builder()
                .userId(shopCartEntity.getUserId())
                .productEntity(productEntity)
                .orderEntity(orderEntity)
                .build();
        // 保存未做营销的普通订单
        this.doSaveOrder(orderAggregate);
        // 营销锁单
        MarketPayDiscountEntity marketPayDiscountEntity = null;
        if (MarketTypeVO.GROUP_BUY_MARKET.equals(shopCartEntity.getMarketTypeVO())) {
            log.info("进行营销锁单 userId:{} productId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId());
            marketPayDiscountEntity = this.lockMarketPayOrder(shopCartEntity.getUserId(),
                    shopCartEntity.getTeamId(),
                    shopCartEntity.getActivityId(),
                    shopCartEntity.getProductId(),
                    orderEntity.getOrderId());
            log.info("营销结果:{}", JSON.toJSONString(marketPayDiscountEntity));
        }
        // 创建支付单
        PayOrderEntity payOrderEntity = doPrepayOrder(shopCartEntity.getUserId(),
                shopCartEntity.getProductId(),
                productEntity.getProductName(),
                orderEntity.getOrderId(),
                productEntity.getPrice(),
                marketPayDiscountEntity);

        log.info("创建订单-完成，生成支付单。userId: {} orderId: {} payUrl: {}", shopCartEntity.getUserId(), orderEntity.getOrderId(), payOrderEntity.getPayUrl());
        return PayOrderEntity.builder()
                .orderId(orderEntity.getOrderId())
                .payUrl(payOrderEntity.getPayUrl())
                .build();
    }

    /**
     * 营销锁单 - 预支付
     * @param userId
     * @param productId
     * @param productName
     * @param orderId
     * @param totalAmount
     * @param marketPayDiscountEntity
     * @return
     */
    protected abstract PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount, MarketPayDiscountEntity marketPayDiscountEntity) throws IOException;

    /**
     * 锁单
     * @param userId
     * @param teamId
     * @param activityId
     * @param productId
     * @param orderId
     * @return
     */
    protected abstract MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId);

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
