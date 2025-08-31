package grocery.service;

import grocery.dto.AuthRequest;
import grocery.dto.AuthResponse;
import grocery.dto.RegisterRequest;


/**
 * Service interface for authentication and user registration.
 */
public interface AuthService {

    AuthResponse login(AuthRequest loginRequest);

    void register(RegisterRequest registerRequest);
}