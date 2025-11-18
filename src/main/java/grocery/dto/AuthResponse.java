package grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for authentication responses.
 * Sends the JWT token back to the client after a successful login.
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    // The JWT token for the authenticated user.
    private String token;
}
