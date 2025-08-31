package grocery.service;


import grocery.dto.OrderItemDTO;
import grocery.dto.OrderResponseDTO;
import grocery.entity.*;
import grocery.exception.ResourceNotFoundException;
import grocery.repository.CartItemRepository;
import grocery.repository.OrderRepository;
import grocery.repository.ProductRepository;
import grocery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService for order placement and viewing.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Place order for all items in user's cart.
     */
    @Override
    public OrderResponseDTO placeOrder(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Calculate total amount and check stock
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (item.getQuantity() > product.getStockQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product " + product.getProductName());
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // Deduct stock
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Create Order and OrderItems
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .status("CREATED")
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteAll(cartItems);

        return mapToOrderResponseDTO(savedOrder);
    }

    /**
     * Get all orders of a user.
     */
    @Override
    public List<OrderResponseDTO> getOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map Order entity to OrderResponseDTO.
     */
    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getProductId());
                    itemDTO.setProductName(item.getProduct().getProductName());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setOrderItems(itemDTOs);

        return dto;
    }
}
