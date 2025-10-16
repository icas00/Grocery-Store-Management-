
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String username;
    private double amount;
    private String status; // PENDING, PAID
    private LocalDateTime createdAt;
    @PrePersist public void pre(){ this.createdAt = LocalDateTime.now();}
}
