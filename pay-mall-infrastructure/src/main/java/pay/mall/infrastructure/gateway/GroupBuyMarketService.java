package pay.mall.infrastructure.gateway;

import pay.mall.infrastructure.gateway.dto.LockMarketPayOrderRequestDTO;
import pay.mall.infrastructure.gateway.dto.LockMarketPayOrderResponseDTO;
import pay.mall.infrastructure.gateway.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GroupBuyMarketService {

    /**
     * 营销锁单
     * @param requestDTO
     * @return
     */
    @POST("/api/gbm/trade/lock_market_pay_order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);

}
