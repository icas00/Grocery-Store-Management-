package grocery.controller;

import grocery.dto.MockPaymentRequest;
import grocery.dto.OrderResponseDTO;
import grocery.dto.PaymentVerificationRequest;
import grocery.dto.RazorpayOrderResponseDTO;
import grocery.service.OrderService;
import grocery.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for order-related requests.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    // Places a new order from the user's cart.
    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        OrderResponseDTO orderResponse = orderService.placeOrder(userDetails.getUsername());
        return ResponseEntity.ok(orderResponse);
    }

    // Gets the order history for a user.
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderResponseDTO> orders = orderService.getOrders(userDetails.getUsername());
        return ResponseEntity.ok(orders);
    }

    // Gets all orders in the system (for admin).
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Creates a Razorpay payment order.
    @PostMapping("/payment/create")
    public ResponseEntity<RazorpayOrderResponseDTO> createPaymentOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam Long orderId) {
        RazorpayOrderResponseDTO razorpayOrderDetails = paymentService.createRazorpayOrder(orderId);
        return ResponseEntity.ok(razorpayOrderDetails);
    }

    // Verifies the payment after the user completes the Razorpay checkout.
    @PostMapping("/payment/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        boolean success = paymentService.verifyPayment(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (success) {
            return ResponseEntity.ok("Payment verified successfully and order updated.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed.");
        }
    }

    // Allows an admin to force an order's status to PAID for testing.
    @PostMapping("/force-paid/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> forceOrderPaid(@PathVariable Long orderId) {
        orderService.forceUpdateOrderStatus(orderId, "PAID");
        return ResponseEntity.ok("Order status forced to PAID.");
    }
}
