package grocery.repository;

import grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a user by their username.
    Optional<User> findByUsername(String username);

    // Checks if a user exists with a given username.
    boolean existsByUsername(String username);

    // Checks if a user exists with a given email.
    boolean existsByEmail(String email);
}
