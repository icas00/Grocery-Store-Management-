package grocery.auth;

import lombok.*;

/**
 * Represents an authentication request.
 * Holds the username and password from the user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String username;
    private String password;
}
