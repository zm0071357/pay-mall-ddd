package pay.mall.api;

import pay.mall.api.dto.LoginRequestDTO;
import pay.mall.api.dto.LoginResponseDTO;
import pay.mall.api.response.Response;

/**
 * 用户登录服务
 */
public interface UserLoginService {

    /**
     * 登录
     * @param loginRequestDTO
     * @return
     */
    Response<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);
}
