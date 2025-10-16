package grocery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name")
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;
}
