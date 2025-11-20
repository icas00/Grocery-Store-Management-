package grocery.service;


import grocery.dto.CartItemDTO;
import grocery.entity.CartItem;
import grocery.entity.Product;
import grocery.entity.User;
import grocery.exception.ResourceNotFoundException;
import grocery.repository.CartItemRepository;
import grocery.repository.ProductRepository;
import grocery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CartService.
 * Handles the business logic for the shopping cart.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    // Adds an item to the cart. Updates quantity if it's already there.
    @Override
    public void addToCart(String username, CartItemDTO cartItemDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItemDTO.getProductId()));

        // Check if the item is already in the cart.
        CartItem cartItem = cartItemRepository.findByUserAndProduct_ProductId(user, product.getProductId())
                .orElse(CartItem.builder().user(user).product(product).quantity(0).build());

        int newQuantity = cartItem.getQuantity() + cartItemDTO.getQuantity();

        // Make sure there's enough stock.
        if (newQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
    }

    // Updates the quantity of an item already in the cart.
    @Override
    public void updateCartItemQuantity(String username, CartItemDTO cartItemDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItemDTO.getProductId()));

        CartItem cartItem = cartItemRepository.findByUserAndProduct_ProductId(user, product.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item", "productId", cartItemDTO.getProductId()));

        // Ensure the new quantity is not negative.
        if (cartItemDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        // Check for sufficient stock for the new quantity.
        if (cartItemDTO.getQuantity() > product.getStockQuantity()) {
            throw new IllegalArgumentException("Not enough stock available for product " + product.getProductName());
        }

        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem);
    }

    // Removes an item from the cart.
    @Override
    public void removeFromCart(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        CartItem cartItem = cartItemRepository.findByUserAndProduct_ProductId(user, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item", "productId", productId));

        cartItemRepository.delete(cartItem);
    }

    // Gets all items in the user's cart.
    @Override
    public List<CartItemDTO> getCartItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        // Map the entities to DTOs.
        return cartItems.stream()
                .map(item -> {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setProductId(item.getProduct().getProductId());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Clears all items from the cart.
    @Override
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
    }
}
