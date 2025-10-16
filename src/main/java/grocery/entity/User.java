package grocery.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true) private String username;
    private String password;
    // simple roles as strings "ROLE_ADMIN"/"ROLE_USER"
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
}
