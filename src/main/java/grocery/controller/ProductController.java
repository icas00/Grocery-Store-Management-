package grocery.controller;


import grocery.dto.ProductDTO;
import grocery.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for product-related requests.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    // Gets all products with optional search, filtering, and pagination.
    // Admins can see unavailable products.
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        logger.info("User '{}' is Admin: {}", authentication.getName(), isAdmin);

        return ResponseEntity.ok(productService.getAllProducts(search, category, pageable, isAdmin));
    }

    // Gets a product by its ID. Accessible to everyone.
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Creates a new product. Only accessible to admins.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.createProduct(productDTO);
        return ResponseEntity.status(201).body(created);
    }

    // Updates an existing product. Only accessible to admins.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    // Soft-deletes a product (marks as unavailable). Only accessible to admins.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); // This performs the soft delete
        return ResponseEntity.ok("Product soft-deleted successfully");
    }

    // Hard-deletes a product (removes permanently from DB). Only accessible to admins.
    // WARNING: This can cause foreign key constraint violations if the product is referenced elsewhere.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDeleteProduct(@PathVariable Long id) {
        productService.hardDeleteProduct(id);
        return ResponseEntity.ok("Product hard-deleted successfully");
    }
}
