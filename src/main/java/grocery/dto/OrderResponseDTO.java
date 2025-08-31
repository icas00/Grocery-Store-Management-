package grocery.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order response with order details and items.
 */
@Data
public class OrderResponseDTO {

    private Long orderId;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private String status;

    private List<OrderItemDTO> orderItems;
}
