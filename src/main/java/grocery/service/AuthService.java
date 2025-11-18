package grocery.service;

import grocery.dto.AuthRequest;
import grocery.dto.AuthResponse;
import grocery.dto.RegisterRequest;

/**
 * Service interface for authentication (login, registration).
 */
public interface AuthService {

    // Authenticates a user and returns a JWT token.
    AuthResponse login(AuthRequest loginRequest);

    // Registers a new user.
    void register(RegisterRequest registerRequest);
}
