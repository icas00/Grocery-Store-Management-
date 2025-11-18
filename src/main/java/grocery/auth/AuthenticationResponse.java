package grocery.auth;

import lombok.*;

/**
 * Represents an authentication response.
 * Contains the JWT token generated after a successful login.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
}
