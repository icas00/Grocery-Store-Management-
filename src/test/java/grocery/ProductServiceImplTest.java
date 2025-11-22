package grocery;

import grocery.dto.ProductDTO;
import grocery.entity.Product;
import grocery.repository.ProductRepository;
import grocery.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName("Test Apple");
        productDTO.setPrice(BigDecimal.valueOf(1.99));
        productDTO.setStockQuantity(100);

        Product product = Product.builder()
                .productId(1L)
                .productName("Test Apple")
                .price(BigDecimal.valueOf(1.99))
                .stockQuantity(100)
                .isAvailable(true)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO createdProduct = productService.createProduct(productDTO);

        assertNotNull(createdProduct);
        assertEquals("Test Apple", createdProduct.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        Product product = Product.builder().productId(1L).productName("Test Apple").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Test Apple", foundProduct.getProductName());
    }

    @Test
    void testGetAllProducts_ForCustomer() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = Product.builder().productName("Test Apple").build();
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findByIsAvailableTrueAndProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase("", "", pageable))
                .thenReturn(productPage);

        Page<ProductDTO> result = productService.getAllProducts("", "", pageable, false);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findByIsAvailableTrueAndProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void testGetAllProducts_ForAdmin() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = Product.builder().productName("Test Apple").build();
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findByProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase("", "", pageable))
                .thenReturn(productPage);

        Page<ProductDTO> result = productService.getAllProducts("", "", pageable, true);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findByProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void testSoftDeleteProduct() {
        Product product = Product.builder().productId(1L).isAvailable(true).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        assertFalse(product.isAvailable());
        verify(productRepository, times(1)).save(product);
    }
}
