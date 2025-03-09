package pay.mall.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import pay.mall.domain.login.model.entity.LoginEntity;
import pay.mall.infrastructure.dao.po.Login;

@Mapper
public interface LoginDao {

    /**
     * 登录
     * @param loginReq
     * @return
     */
    int queryUserByAccountAndPassWord(Login loginReq);

    /**
     * 添加用户
     * @param user
     */
    void addUser(LoginEntity user);
}
