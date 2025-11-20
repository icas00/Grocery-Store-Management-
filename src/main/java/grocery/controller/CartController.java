package grocery.controller;


import grocery.dto.CartItemDTO;
import grocery.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for shopping cart requests.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Adds an item to the cart.
    // @AuthenticationPrincipal gets the currently logged-in user.
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody CartItemDTO cartItemDTO) {
        cartService.addToCart(userDetails.getUsername(), cartItemDTO);
        return ResponseEntity.ok("Item added to cart");
    }

    // Updates the quantity of an item in the cart.
    @PutMapping("/update")
    public ResponseEntity<String> updateCartItemQuantity(@AuthenticationPrincipal UserDetails userDetails,
                                                         @Valid @RequestBody CartItemDTO cartItemDTO) {
        cartService.updateCartItemQuantity(userDetails.getUsername(), cartItemDTO);
        return ResponseEntity.ok("Cart item quantity updated");
    }

    // Removes an item from the cart.
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long productId) {
        cartService.removeFromCart(userDetails.getUsername(), productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    // Gets all items in the cart.
    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCartItems(userDetails.getUsername()));
    }
}
