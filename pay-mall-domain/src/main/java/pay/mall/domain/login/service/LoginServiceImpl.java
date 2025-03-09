package pay.mall.domain.login.service;

import cn.dev33.satoken.stp.StpUtil;
import pay.mall.domain.login.adapter.repository.LoginRepository;
import pay.mall.domain.login.model.entity.LoginEntity;
import pay.mall.domain.login.model.valobj.LoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    private LoginRepository loginRepository;

    @Override
    public LoginInfoVO doLogin(LoginEntity loginEntity) {
        // 登录
        int isLogin = loginRepository.login(loginEntity);
        // 登录成功
        if (isLogin == 1) {
            // SaToken进行登录操作
            StpUtil.login(loginEntity.getAccount());
            log.info("用户:{}登录成功", loginEntity.getAccount());
            return LoginInfoVO.builder()
                    .isSuccess(0)
                    .message("登录成功")
                    .build();
        }
        // 登录失败
        log.info("用户:{}登录失败", loginEntity.getAccount());
        return LoginInfoVO.builder()
                .isSuccess(1)
                .message("登录失败，请检查账号/密码是否正确")
                .build();
    }
}
