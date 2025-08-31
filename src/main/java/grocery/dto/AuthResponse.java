package grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for login response containing JWT token.
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
}