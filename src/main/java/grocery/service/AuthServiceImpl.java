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
 * Implementation of AuthService that handles login and registration logic.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Authenticate user and generate JWT token.
     */
    @Override
    public AuthResponse login(AuthRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return new AuthResponse(token);
    }

    /**
     * Register a new user with role CUSTOMER.
     */
    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new CustomException("Username already taken");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException("Email already in use");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        Set<Role> roles = new HashSet<>();

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new CustomException("Customer Role not set in database"));
        roles.add(customerRole);

        user.setRoles(roles);

        userRepository.save(user);
    }
}