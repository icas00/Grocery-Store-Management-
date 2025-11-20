package grocery.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an item in a user's shopping cart.
 * Links a user, a product, and the quantity.
 */
@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who owns this cart item.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The product added to the cart.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Quantity of the product.
    @Column(nullable = false)
    private Integer quantity;
}
