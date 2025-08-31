package grocery.service;


import grocery.dto.ProductDTO;
import grocery.entity.Product;
import grocery.exception.ResourceNotFoundException;
import grocery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService to handle product CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Create a new product.
     */
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    /**
     * Update existing product.
     */
    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        product.setProductName(productDTO.getProductName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        Product updatedProduct = productRepository.save(product);
        return mapToDTO(updatedProduct);
    }

    /**
     * Delete product by id.
     */
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productRepository.delete(product);
    }

    /**
     * Get product by id.
     */
    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return mapToDTO(product);
    }

    /**
     * Get all products.
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map Product entity to ProductDTO.
     */
    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        return dto;
    }

    /**
     * Map ProductDTO to entity.
     */
    private Product mapToEntity(ProductDTO dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .build();
    }
}
