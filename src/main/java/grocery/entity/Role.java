package grocery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Role name, e.g. ROLE_ADMIN, ROLE_CUSTOMER
    @Column(nullable = false, unique = true)
    private String name;
}