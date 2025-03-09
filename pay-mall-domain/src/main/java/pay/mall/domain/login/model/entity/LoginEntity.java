package pay.mall.domain.login.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginEntity {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
