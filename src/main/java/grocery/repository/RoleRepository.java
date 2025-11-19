package grocery.repository;

import grocery.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for the Role entity.
 * JpaRepository provides basic CRUD operations.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Finds a role by its name.
    Optional<Role> findByName(String name);
}
