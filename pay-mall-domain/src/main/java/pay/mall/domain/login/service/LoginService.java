package pay.mall.domain.login.service;

import pay.mall.domain.login.model.entity.LoginEntity;
import pay.mall.domain.login.model.valobj.LoginInfoVO;

/**
 * 登录服务
 */
public interface LoginService {

    /**
     * 登录
     * @param loginEntity
     * @return
     */
    LoginInfoVO doLogin(LoginEntity loginEntity);
}
