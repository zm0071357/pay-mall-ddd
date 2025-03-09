package pay.mall.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import pay.mall.api.dto.CreatePayRequestDTO;
import pay.mall.api.response.Response;

/**
 * 支付服务
 */
public interface PayService {

    /**
     * 支付
     * @param createPayRequestDTO
     * @return
     */
    Response<String> createPayOrder(CreatePayRequestDTO createPayRequestDTO);

    /**
     * 支付回调通知用户
     * 支付完成后，蓝兔支付会把相关支付结果和用户信息发送给商户
     * @param code 支付结果，枚举值：0：成功 1：失败
     * @param timestamp 时间戳
     * @param mch_id 商户号
     * @param order_no 系统订单号
     * @param out_trade_no 订单号
     * @param pay_no 微信支付订单号
     * @param total_fee 金额
     * @param sign 签名
     * @param pay_channel 支付渠道
     * @param trade_type 支付类型
     * @param success_time 支付完成时间
     * @param attach 附加数据
     * @param openid 支付者信息
     * @return
     */
    ResponseEntity<String> payNotify(
            String code,
            String timestamp,
            String mch_id,
            String order_no,
            String out_trade_no,
            String pay_no,
            String total_fee,
            String sign,
            String pay_channel,
            String trade_type,
            String success_time,
            String attach,
            String openid);

    /**
     * 退款回调通知
     * @param code 支付结果，枚举值：0：成功 1：失败
     * @param timestamp 时间戳
     * @param mch_id 商户号
     * @param order_no 系统订单号
     * @param out_trade_no 订单号
     * @param pay_no 微信支付订单号
     * @param refund_no 系统退款单号
     * @param out_refund_no 退款单号
     * @param pay_channel 退款渠道
     * @param refund_fee 金额
     * @param sign 签名
     * @param success_time 退款完成时间
     * @return
     */
    ResponseEntity<String> refundNotify(
            String code,
            String timestamp,
            String mch_id,
            String order_no,
            String out_trade_no,
            String pay_no,
            String refund_no,
            String out_refund_no,
            String pay_channel,
            String refund_fee,
            String sign,
            String success_time);
}
