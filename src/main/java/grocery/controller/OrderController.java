package grocery.controller;

import grocery.dto.OrderResponseDTO;
import grocery.service.OrderService;
import grocery.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

   
    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        OrderResponseDTO orderResponse = orderService.placeOrder(userDetails.getUsername());
        return ResponseEntity.ok(orderResponse);
    }

    
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderResponseDTO> orders = orderService.getOrders(userDetails.getUsername());
        return ResponseEntity.ok(orders);
    }

   
    @PostMapping("/payment/create")
    public ResponseEntity<String> createPaymentOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam Long orderId) {
        String paymentOrderId = paymentService.createRazorpayOrder(orderId);
        return ResponseEntity.ok(paymentOrderId);
    }
}
