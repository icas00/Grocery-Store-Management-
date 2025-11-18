package grocery.service;


import grocery.dto.AuthRequest;
import grocery.dto.AuthResponse;
import grocery.dto.RegisterRequest;
import grocery.entity.Role;
import grocery.entity.User;
import grocery.exception.CustomException;
import grocery.repository.RoleRepository;
import grocery.repository.UserRepository;
import grocery.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of AuthService.
 * Handles the business logic for login and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Handles the user login process.
    @Override
    public AuthResponse login(AuthRequest loginRequest) {
        try {
            // Use the AuthenticationManager to validate credentials.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Invalid username or password");
        }

        // If successful, generate a JWT token.
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return new AuthResponse(token);
    }

    // Handles the user registration process.
    @Override
    public void register(RegisterRequest registerRequest) {
        // Check if username or email is already taken.
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new CustomException("Username already taken");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException("Email already in use");
        }

        // Create a new user and encode the password.
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        // Assign the default 'ROLE_CUSTOMER' to the new user.
        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new CustomException("Customer Role not set in database"));
        roles.add(customerRole);
        user.setRoles(roles);

        userRepository.save(user);
    }
}
