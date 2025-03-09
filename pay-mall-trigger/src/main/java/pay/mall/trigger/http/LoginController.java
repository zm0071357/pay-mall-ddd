package pay.mall.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pay.mall.api.UserLoginService;
import pay.mall.api.dto.LoginRequestDTO;
import pay.mall.api.dto.LoginResponseDTO;
import pay.mall.api.response.Response;
import pay.mall.domain.login.model.entity.LoginEntity;
import pay.mall.domain.login.model.valobj.LoginInfoVO;
import pay.mall.domain.login.service.LoginService;
import pay.mall.types.enums.ResponseCode;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/login")
public class LoginController implements UserLoginService {

    @Resource
    private LoginService loginService;

    @PostMapping("/doLogin")
    @Override
    public Response<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // 参数
        String account = loginRequestDTO.getAccount();
        String password = loginRequestDTO.getPassword();
        log.info("用户:{}进行登录", account);
        // 校验
        if (account == null) {
            log.info("登录失败，缺少账号");
            return Response.<LoginResponseDTO>builder()
                    .code(ResponseCode.LACK_ACCOUNT.getCode())
                    .info(ResponseCode.LACK_ACCOUNT.getInfo())
                    .data(LoginResponseDTO.builder()
                            .isSuccess(1)
                            .message("登录失败")
                            .build())
                    .build();
        }
        if (password == null) {
            log.info("用户:{}登录失败，缺少密码", account);
            return Response.<LoginResponseDTO>builder()
                    .code(ResponseCode.LACK_PASSWORD.getCode())
                    .info(ResponseCode.LACK_PASSWORD.getInfo())
                    .data(LoginResponseDTO.builder()
                            .isSuccess(1)
                            .message("登录失败")
                            .build())
                    .build();
        }
        LoginEntity loginEntity = LoginEntity.builder()
                .account(account)
                .password(password).build();
        // 登录
        LoginInfoVO loginInfoVO = loginService.doLogin(loginEntity);
        return Response.<LoginResponseDTO>builder()
                .code(loginInfoVO.getIsSuccess() == 0 ? ResponseCode.SUCCESS.getCode() : ResponseCode.ACCOUNT_OR_PASSWORD_ERROR.getCode())
                .info(loginInfoVO.getIsSuccess() == 0 ? ResponseCode.SUCCESS.getInfo() : ResponseCode.ACCOUNT_OR_PASSWORD_ERROR.getInfo())
                .data(LoginResponseDTO.builder()
                        .isSuccess(loginInfoVO.getIsSuccess())
                        .message(loginInfoVO.getMessage())
                        .build())
                .build();
    }

}
