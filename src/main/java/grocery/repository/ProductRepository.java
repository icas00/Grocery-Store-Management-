package grocery.repository;



import grocery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Product entity.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}