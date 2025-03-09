package pay.mall.infrastructure.adapter.port;

import org.springframework.stereotype.Component;
import pay.mall.domain.order.adapter.port.ProductPort;
import pay.mall.domain.order.model.entity.ProductEntity;
import pay.mall.infrastructure.gateway.ProductRPC;
import pay.mall.infrastructure.gateway.dto.ProductDTO;

@Component
public class ProductPortImpl implements ProductPort {

    private final ProductRPC productRPC;

    public ProductPortImpl(ProductRPC productRPC) {
        this.productRPC = productRPC;
    }

    @Override
    public ProductEntity queryProductByProductId(String productId) {
        ProductDTO productDTO = productRPC.queryProductByProductId(productId);
        return ProductEntity.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productDesc(productDTO.getProductDesc())
                .price(productDTO.getPrice())
                .build();
    }
}
