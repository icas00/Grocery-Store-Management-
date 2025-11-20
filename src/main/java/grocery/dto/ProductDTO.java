package grocery.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for product data.
 * Used to send product info between the client and server.
 */
@Data
public class ProductDTO {

    private Long productId;

    // Product name can't be blank.
    @NotBlank(message = "Product name is required")
    @Size(max = 100)
    private String productName;

    @Size(max = 50)
    private String category;

    // Price must be a positive number.
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    // Stock can't be negative.
    @NotNull(message = "Stock quantity is required")
    @Min(0)
    private Integer stockQuantity;

    private boolean isAvailable;
}
