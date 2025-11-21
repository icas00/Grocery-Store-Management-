package grocery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user registration requests.
 * Carries the info needed to create a new user.
 */
@Data
public class RegisterRequest {

    // Username for the new account.
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    // Password for the new account.
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100)
    private String password;

    // Email for the new account, must be a valid format.
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
