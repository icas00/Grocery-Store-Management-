package grocery.service;

import grocery.dto.AuthRequest;
import grocery.dto.AuthResponse;
import grocery.dto.RegisterRequest;



public interface AuthService {

    AuthResponse login(AuthRequest loginRequest);

    void register(RegisterRequest registerRequest);
}
