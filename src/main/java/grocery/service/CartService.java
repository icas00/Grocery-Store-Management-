package grocery.service;


import grocery.dto.CartItemDTO;

import java.util.List;


public interface CartService {

    void addToCart(String username, CartItemDTO cartItemDTO);

    void removeFromCart(String username, Long productId);

    List<CartItemDTO> getCartItems(String username);

    void clearCart(String username);
}
