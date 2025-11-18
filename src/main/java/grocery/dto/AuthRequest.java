package grocery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for login requests.
 * Carries the user's login details.
 */
@Data
public class AuthRequest {

    // The user's username.
    @NotBlank(message = "Username is required")
    private String username;

    // The user's password.
    @NotBlank(message = "Password is required")
    private String password;
}
