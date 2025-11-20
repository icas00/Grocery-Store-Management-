package grocery.service;


import grocery.dto.OrderResponseDTO;

import java.util.List;

/**
 * Service interface for order management.
 */
public interface OrderService {

    // Places a new order from the user's cart.
    OrderResponseDTO placeOrder(String username);

    // Gets the order history for a user.
    List<OrderResponseDTO> getOrders(String username);

    // Gets all orders in the system (typically for admin).
    List<OrderResponseDTO> getAllOrders();

    // For testing: forces an order's status to be updated.
    void forceUpdateOrderStatus(Long orderId, String status);
}
