package grocery.dto;

import lombok.Data;

import java.util.Set;

/**
 * DTO for user profile data.
 * Safely exposes user information without the password.
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles; // Added to include user roles
}
