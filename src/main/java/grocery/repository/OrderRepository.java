package grocery.repository;

import grocery.entity.Order;
import grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Orders.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}