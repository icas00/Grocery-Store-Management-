package grocery.service;

import grocery.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for product management.
 */
public interface ProductService {

    // Creates a new product.
    ProductDTO createProduct(ProductDTO productDTO);

    // Updates an existing product.
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    // Soft-deletes a product (marks as unavailable).
    void deleteProduct(Long productId);

    // Hard-deletes a product (removes permanently from DB).
    void hardDeleteProduct(Long productId);

    // Gets a product by its ID.
    ProductDTO getProductById(Long productId);

    // Gets all products with optional filtering and pagination.
    // includeUnavailable: if true, also returns products marked as unavailable (for admins).
    Page<ProductDTO> getAllProducts(String search, String category, Pageable pageable, boolean includeUnavailable);
}
