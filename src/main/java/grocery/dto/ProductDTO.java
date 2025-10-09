package grocery.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 100)
    private String productName;

    @Size(max = 50)
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(0)
    private Integer stockQuantity;
}
