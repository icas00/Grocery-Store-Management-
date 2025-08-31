package grocery.service;


import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import grocery.entity.Order;

import grocery.exception.ResourceNotFoundException;
import grocery.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementation of PaymentService integrating with Razorpay.
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    private final OrderRepository orderRepository;

    /**
     * Initialize Razorpay client.
     */
    @PostConstruct
    public void init() {
        try {
            razorpayClient = new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to initialize Razorpay client", e);
        }
    }

    /**
     * Create Razorpay order for payment using order amount.
     */
    @Override
    public String createRazorpayOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        JSONObject options = new JSONObject();
        int amountInPaise = order.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue();

        options.put("amount", amountInPaise); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + orderId);

        try {
            com.razorpay.Order razorpayOrder = razorpayClient.Orders.create(options);
            return razorpayOrder.get("id");
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }
}
