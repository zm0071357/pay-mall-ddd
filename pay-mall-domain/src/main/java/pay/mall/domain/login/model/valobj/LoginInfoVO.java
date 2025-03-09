package pay.mall.domain.login.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoVO {

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
