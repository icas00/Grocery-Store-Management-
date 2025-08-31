package grocery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for cart item add/remove requests.
 */
@Data
public class CartItemDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(1)
    private Integer quantity;
}