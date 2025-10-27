package grocery.controller;

import grocery.entity.User;
import grocery.repository.UserRepository;
import grocery.security.JwtUtil;
import grocery.service.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SimpleUserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body){
        // simple beginner validation
        String username = body.get("username");
        String password = body.get("password");
        boolean isAdmin = "true".equals(body.get("admin"));
        User u = userService.register(username, password, isAdmin);
        return ResponseEntity.ok(Map.of("message","registered","username",u.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String username = body.get("username");
        String pass = body.get("password");
        var user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));

        if(!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(pass, user.getPassword())){
            return ResponseEntity.status(401).body(Map.of("error","invalid"));
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());
    
        System.out.println("[DEBUG] Generated token for " + username + ": " + token.substring(0, Math.min(20, token.length())) + "...");
        return ResponseEntity.ok(Map.of("token", token));
    }
}
