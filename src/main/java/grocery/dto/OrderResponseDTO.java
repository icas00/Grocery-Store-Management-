package grocery.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order responses.
 * Used to show order history to the user.
 */
@Data
public class OrderResponseDTO {

    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDTO> orderItems;
}
