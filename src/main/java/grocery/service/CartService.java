package grocery.service;


import grocery.dto.CartItemDTO;

import java.util.List;

/**
 * Service interface for shopping cart operations.
 */
public interface CartService {

    // Adds an item to the user's cart.
    void addToCart(String username, CartItemDTO cartItemDTO);

    // Updates the quantity of an item already in the cart.
    void updateCartItemQuantity(String username, CartItemDTO cartItemDTO);

    // Removes an item from the user's cart.
    void removeFromCart(String username, Long productId);

    // Gets all items from the user's cart.
    List<CartItemDTO> getCartItems(String username);

    // Clears all items from the user's cart.
    void clearCart(String username);
}
