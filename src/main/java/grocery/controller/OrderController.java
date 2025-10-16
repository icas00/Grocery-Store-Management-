package grocery.controller;

import grocery.entity.OrderEntity;
import grocery.repository.OrderRepository;
import grocery.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") String auth, @RequestBody Map<String,Object> body){
        String token = auth.substring(7);
        if(!jwtUtil.validate(token)) return ResponseEntity.status(401).body("invalid");
        String user = jwtUtil.extractUsername(token);
        double amount = Double.parseDouble(body.getOrDefault("amount","0").toString());
        OrderEntity o = OrderEntity.builder().username(user).amount(amount).status("PENDING").build();
        orderRepository.save(o);

        return ResponseEntity.ok(Map.of("orderId", o.getId(), "status", o.getStatus()));
    }
}
