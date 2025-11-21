package grocery.repository;

import grocery.entity.CartItem;
import grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the CartItem entity.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Finds all cart items for a given user.
    List<CartItem> findByUser(User user);

    // Finds a specific cart item by user and product ID.
    Optional<CartItem> findByUserAndProduct_ProductId(User user, Long productId);
}
