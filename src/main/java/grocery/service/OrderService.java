package grocery.service;


import grocery.dto.OrderResponseDTO;

import java.util.List;

/**
 * Service interface for order placement and retrieval.
 */
public interface OrderService {

    OrderResponseDTO placeOrder(String username);

    List<OrderResponseDTO> getOrders(String username);
}
