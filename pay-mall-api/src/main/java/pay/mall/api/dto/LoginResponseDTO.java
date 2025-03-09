package pay.mall.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求应答对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    /**
     * 登录是否成功
     * 0 成功
     * 1 失败
     */
    private Integer isSuccess;

    /**
     * message
     */
    private String message;
}
