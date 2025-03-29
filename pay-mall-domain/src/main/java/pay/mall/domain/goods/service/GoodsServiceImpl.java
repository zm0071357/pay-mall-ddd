package pay.mall.domain.goods.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pay.mall.domain.goods.adapter.repository.GoodsRepository;

import javax.annotation.Resource;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsRepository goodsRepository;


    @Override
    public void changeOrderDealDone(String tradeNo) {
        goodsRepository.changeOrderDealDone(tradeNo);
    }
}
