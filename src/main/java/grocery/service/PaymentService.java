package grocery.service;

/**
 * Service interface for payment-related operations.
 */
public interface PaymentService {

    String createRazorpayOrder(Long orderId);
}