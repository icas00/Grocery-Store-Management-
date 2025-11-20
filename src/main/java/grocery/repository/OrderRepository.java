package grocery.repository;

import grocery.entity.Order;
import grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the Order entity.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Finds all orders for a given user.
    List<Order> findByUser(User user);

    // Finds an order by its Razorpay order ID.
    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);
}
