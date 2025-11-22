package grocery.service;

import grocery.dto.ProductDTO;
import grocery.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    /**
     * This method maps a Product entity to a ProductDTO.
     * @param product The Product entity to map.
     * @return The resulting ProductDTO.
     */
    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setAvailable(product.isAvailable());
        return dto;
    }

    /**
     * This method maps a ProductDTO to a Product entity.
     * When creating a new product, it defaults its availability to true.
     * @param dto The ProductDTO to map.
     * @return The resulting Product entity.
     */
    public Product toEntity(ProductDTO dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .isAvailable(dto.isAvailable())
                .build();
    }
}