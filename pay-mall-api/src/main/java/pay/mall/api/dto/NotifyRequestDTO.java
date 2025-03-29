package pay.mall.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 回调请求对象
 */
@Data
public class NotifyRequestDTO {

    /**
     * 拼团ID
     */
    private String teamId;

    /**
     * 外部交易单号集合
     */
    private List<String> outTradeNoList;

}
