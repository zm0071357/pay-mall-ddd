package pay.mall.api.dto;

import lombok.Data;

/**
 * 登录请求对象
 */
@Data
public class LoginRequestDTO {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
