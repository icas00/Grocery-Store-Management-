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


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody CartItemDTO cartItemDTO) {
        cartService.addToCart(userDetails.getUsername(), cartItemDTO);
        return ResponseEntity.ok("Item added to cart");
    }

    
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long productId) {
        cartService.removeFromCart(userDetails.getUsername(), productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    
    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCartItems(userDetails.getUsername()));
    }
}
