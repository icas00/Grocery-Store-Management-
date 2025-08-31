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
 * Implementation of CartService to manage cart items.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * Add product to user's cart or update quantity.
     */
    @Override
    public void addToCart(String username, CartItemDTO cartItemDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItemDTO.getProductId()));

        CartItem cartItem = cartItemRepository.findByUserAndProduct_ProductId(user, product.getProductId())
                .orElse(CartItem.builder().user(user).product(product).quantity(0).build());

        int newQuantity = cartItem.getQuantity() + cartItemDTO.getQuantity();

        if (newQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
    }

    /**
     * Remove product from user's cart.
     */
    @Override
    public void removeFromCart(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        CartItem cartItem = cartItemRepository.findByUserAndProduct_ProductId(user, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item", "productId", productId));

        cartItemRepository.delete(cartItem);
    }

    /**
     * Get all cart items for a user.
     */
    @Override
    public List<CartItemDTO> getCartItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        return cartItems.stream()
                .map(item -> {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setProductId(item.getProduct().getProductId());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Clear all items from user's cart.
     */
    @Override
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
    }
}