package grocery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for items in the shopping cart.
 */
@Data
public class CartItemDTO {

    // ID of the product in the cart.
    @NotNull(message = "Product ID is required")
    private Long productId;

    // Quantity must be at least 1.
    @NotNull(message = "Quantity is required")
    @Min(1)
    private Integer quantity;
}
