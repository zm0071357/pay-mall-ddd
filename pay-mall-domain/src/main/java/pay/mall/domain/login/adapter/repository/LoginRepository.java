package pay.mall.domain.login.adapter.repository;

import pay.mall.domain.login.model.entity.LoginEntity;

/**
 * 登录仓储
 */
public interface LoginRepository {

    /**
     * 登录
     * @param loginEntity
     * @return
     */
    int login(LoginEntity loginEntity);
}
