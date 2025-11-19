package grocery.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user role (e.g., 'ROLE_CUSTOMER', 'ROLE_ADMIN').
 * Used for authorization.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Role name must be unique.
    @Column(nullable = false, unique = true)
    private String name;
}
