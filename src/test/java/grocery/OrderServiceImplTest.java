package grocery;

import grocery.dto.OrderResponseDTO;
import grocery.entity.CartItem;
import grocery.entity.Product;
import grocery.entity.User;
import grocery.repository.CartItemRepository;
import grocery.repository.OrderRepository;
import grocery.repository.ProductRepository;
import grocery.repository.UserRepository;
import grocery.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testPlaceOrder_Success() {
        User user = new User();
        user.setUsername("testuser");

        Product product = Product.builder()
                .productId(1L)
                .productName("Test Apple")
                .price(BigDecimal.valueOf(1.99))
                .stockQuantity(10)
                .build();

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(2)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser(user)).thenReturn(Collections.singletonList(cartItem));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDTO orderResponse = orderService.placeOrder("testuser");

        assertNotNull(orderResponse);
        assertEquals("CREATED", orderResponse.getStatus());
        assertEquals(8, product.getStockQuantity()); // 10 - 2 = 8
        verify(productRepository, times(1)).save(product);
        verify(cartItemRepository, times(1)).deleteAll(any());
    }

    @Test
    void testPlaceOrder_InsufficientStock() {
        User user = new User();
        user.setUsername("testuser");

        Product product = Product.builder()
                .productId(1L)
                .productName("Test Apple")
                .price(BigDecimal.valueOf(1.99))
                .stockQuantity(1) // Only 1 in stock
                .build();

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(2) // Trying to order 2
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser(user)).thenReturn(Collections.singletonList(cartItem));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder("testuser");
        });

        assertEquals("Not enough stock for product Test Apple", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
}
