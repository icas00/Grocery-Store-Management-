package grocery.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for a single item in an order.
 * Shows a snapshot of the product details at the time of purchase.
 */
@Data
public class OrderItemDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
