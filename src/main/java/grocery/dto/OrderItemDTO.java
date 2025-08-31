package grocery.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for individual order item details.
 */
@Data
    public class OrderItemDTO {

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal price;
}
