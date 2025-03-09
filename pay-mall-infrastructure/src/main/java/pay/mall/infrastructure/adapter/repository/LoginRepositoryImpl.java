package pay.mall.infrastructure.adapter.repository;

import pay.mall.domain.login.adapter.repository.LoginRepository;
import pay.mall.domain.login.model.entity.LoginEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pay.mall.infrastructure.dao.LoginDao;
import pay.mall.infrastructure.dao.po.Login;
import pay.mall.types.utils.MD5Utils;

import javax.annotation.Resource;

@Repository
@Slf4j
public class LoginRepositoryImpl implements LoginRepository {

    @Resource
    private LoginDao loginDao;

    @Override
    public int login(LoginEntity loginEntity) {
        // 密码进行加密处理
        String password = loginEntity.getPassword();
        String saltPassword = MD5Utils.MD5(password);
        Login loginReq = new Login();
        loginReq.setAccount(loginEntity.getAccount());
        loginReq.setPassword(saltPassword);
        return loginDao.queryUserByAccountAndPassWord(loginReq);
    }
}
