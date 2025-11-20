package grocery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a single item within an order.
 * Captures the product, quantity, and price at the time of purchase.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // The order this item belongs to.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // The product that was ordered.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Quantity ordered.
    @Column(nullable = false)
    private Integer quantity;

    // Price at the time of the order.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
