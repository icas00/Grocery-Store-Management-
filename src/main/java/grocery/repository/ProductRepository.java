package grocery.repository;

import grocery.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Product entity.
 * JpaRepository provides basic CRUD operations.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds all available products, filtered by name and category, with pagination.
     * @param productName The search term for the product name (case-insensitive).
     * @param category The search term for the category (case-insensitive).
     * @param pageable Pagination information (page number, size).
     * @return A page of matching products.
     */
    Page<Product> findByIsAvailableTrueAndProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String productName, String category, Pageable pageable);

    /**
     * Finds all products (including unavailable ones), filtered by name and category, with pagination.
     * This is typically for admin views.
     * @param productName The search term for the product name (case-insensitive).
     * @param category The search term for the category (case-insensitive).
     * @param pageable Pagination information (page number, size).
     * @return A page of matching products.
     */
    Page<Product> findByProductNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String productName, String category, Pageable pageable);
}
