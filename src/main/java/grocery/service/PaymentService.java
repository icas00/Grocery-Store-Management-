package grocery.service;

import grocery.dto.RazorpayOrderResponseDTO;

/**
 * Service interface for payment operations.
 */
public interface PaymentService {

    // Creates a Razorpay order for a given order ID.
    RazorpayOrderResponseDTO createRazorpayOrder(Long orderId);

    // Verifies the Razorpay payment signature.
    boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);

    // Mocks payment verification by checking status directly with Razorpay.
    // For backend testing.
    boolean mockVerifyPayment(String razorpayOrderId);
}
