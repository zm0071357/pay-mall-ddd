package pay.mall.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pay.mall.domain.goods.adapter.repository.GoodsRepository;
import pay.mall.infrastructure.dao.PayOrderDao;

import javax.annotation.Resource;

@Repository
@Slf4j
public class GoodsRepositoryImpl implements GoodsRepository {

    @Resource
    private PayOrderDao payOrderDao;

    @Override
    public void changeOrderDealDone(String tradeNo) {
        payOrderDao.changeOrderDealDone(tradeNo);
    }
}
