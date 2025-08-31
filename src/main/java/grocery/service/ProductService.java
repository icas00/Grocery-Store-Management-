package grocery.service;

import grocery.dto.ProductDTO;

import java.util.List;

/**
 * Service interface for product management.
 */
public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    void deleteProduct(Long productId);

    ProductDTO getProductById(Long productId);

    List<ProductDTO> getAllProducts();
}