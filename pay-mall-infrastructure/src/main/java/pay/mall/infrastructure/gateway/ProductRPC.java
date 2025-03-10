package pay.mall.infrastructure.gateway;

import org.springframework.stereotype.Service;
import pay.mall.infrastructure.gateway.dto.ProductDTO;

import java.math.BigDecimal;

@Service
public class ProductRPC {

    /**
     * 伪查询商品
     * @param productId
     * @return
     */
    public ProductDTO queryProductByProductId(String productId){
        ProductDTO productVO = new ProductDTO();
        productVO.setProductId(productId);
        productVO.setProductName("《手写MyBatis：渐进式源码实践》");
        productVO.setProductDesc("暂无");
        productVO.setPrice(new BigDecimal("100.00"));
        return productVO;
    }

}
