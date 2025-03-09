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
        productVO.setProductName("脆生生口味薯条");
        productVO.setProductDesc("原切薯条休闲解馋小零食");
        productVO.setPrice(new BigDecimal("1.68"));
        return productVO;
    }

}
