package grocery.service;


import grocery.dto.ProductDTO;
import grocery.entity.Product;
import grocery.exception.ResourceNotFoundException;
import grocery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService.
 * Handles product CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // Creates a new product.
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    // Updates an existing product.
    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        product.setProductName(productDTO.getProductName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setAvailable(productDTO.isAvailable());

        Product updatedProduct = productRepository.save(product);
        return mapToDTO(updatedProduct);
    }

    // Soft-deletes a product by marking it as unavailable.
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setAvailable(false);
        productRepository.save(product);
    }

    // Hard-deletes a product (removes permanently from DB).
    @Override
    public void hardDeleteProduct(Long productId) {
        // Check if product exists before deleting.
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        productRepository.deleteById(productId);
    }

    // Gets a product by its ID.
    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return mapToDTO(product);
    }

    // Gets all products with optional filtering and pagination.
    // Conditionally includes unavailable products based on the flag.
    @Override
    public Page<ProductDTO> getAllProducts(String search, String category, Pageable pageable, boolean includeUnavailable) {
        String productNameSearch = (search == null) ? "" : search;
        String categorySearch = (category == null) ? "" : category;

        Page<Product> products;
        if (includeUnavailable) {
            products = productRepository.findByProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                    productNameSearch, categorySearch, pageable);
        } else {
            products = productRepository.findByIsAvailableTrueAndProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                    productNameSearch, categorySearch, pageable);
        }
        
        return products.map(this::mapToDTO);
    }

    // Helper to map a Product entity to a DTO.
    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setAvailable(product.isAvailable());
        return dto;
    }

    // Helper to map a ProductDTO to an entity.
    private Product mapToEntity(ProductDTO dto) {
        return Product.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .isAvailable(dto.isAvailable())
                .build();
    }
}
