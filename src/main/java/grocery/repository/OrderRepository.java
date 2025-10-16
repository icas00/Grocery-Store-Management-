package grocery.repository;

import grocery.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUsername(String username);
}
