package pay.mall.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pay.mall.api.PayService;
import pay.mall.api.dto.CreatePayRequestDTO;
import pay.mall.api.response.Response;
import pay.mall.domain.order.model.entity.PayOrderEntity;
import pay.mall.domain.order.model.entity.ShopCartEntity;
import pay.mall.domain.order.service.OrderService;
import pay.mall.types.enums.ResponseCode;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/pay")
public class PayController implements PayService {

    @Resource
    private OrderService orderService;

    @PostMapping("/create_pay_order")
    @Override
    public Response<String> createPayOrder(@RequestBody CreatePayRequestDTO createPayRequestDTO) {
        try {
            log.info("商品下单，根据商品ID创建支付单开始 userId:{} productId:{}", createPayRequestDTO.getUserId(), createPayRequestDTO.getProductId());
            String userId = createPayRequestDTO.getUserId();
            String productId = createPayRequestDTO.getProductId();
            PayOrderEntity payOrderEntity = orderService.createOrder(ShopCartEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .build());
            log.info("商品下单，根据商品ID创建支付单完成 userId:{} productId:{} orderId:{}", userId, productId, payOrderEntity.getOrderId());
            return Response.<String>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(payOrderEntity.getPayUrl())
                    .build();
        } catch (Exception e) {
            log.error("商品下单，根据商品ID创建支付单失败 userId:{} productId:{}", createPayRequestDTO.getUserId(), createPayRequestDTO.getProductId(), e);
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping("/pay_notify")
    @Override
    public ResponseEntity<String> payNotify(String code, String timestamp, String mch_id, String order_no, String out_trade_no, String pay_no, String total_fee, String sign, String pay_channel, String trade_type, String success_time, String attach, String openid) {
        try {
            log.info("支付回调，请求参数：{} {} {} {} {} {} {} {} {} {} {} {} {}",
                    code, timestamp, mch_id, order_no, out_trade_no, pay_no, total_fee,
                    sign, pay_channel, trade_type, success_time, attach, openid);
            // 支付完成
            if (code.equals("0") && success_time != null) {
                orderService.changeOrderPaySuccess(out_trade_no);
            }
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refund_notify")
    @Override
    public ResponseEntity<String> refundNotify(String code, String timestamp, String mch_id, String order_no, String out_trade_no, String pay_no, String refund_no, String out_refund_no, String pay_channel, String refund_fee, String sign, String success_time) {
        try {
            log.info("请求参数：{} {} {} {} {} {} {} {} {} {} {} {}",
                    code, timestamp, mch_id, order_no, out_trade_no, pay_no, refund_no,
                    out_refund_no, pay_channel, refund_fee, sign, success_time);
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

}
