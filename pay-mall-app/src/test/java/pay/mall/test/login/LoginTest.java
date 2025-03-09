package pay.mall.test.login;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pay.mall.domain.login.model.entity.LoginEntity;
import pay.mall.infrastructure.dao.LoginDao;
import pay.mall.types.utils.MD5Utils;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {

    @Resource
    private LoginDao loginDao;

    @Test
    public void addUser() {
        LoginEntity user = LoginEntity.builder()
                .account("lzm0071359")
                .password(MD5Utils.MD5("123456"))
                .build();
        loginDao.addUser(user);
    }
}
