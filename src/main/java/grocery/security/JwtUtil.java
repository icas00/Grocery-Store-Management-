package grocery.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;  // saved this secret in application.properties

    @Value("${jwt.expiration-ms}")
    private long expMs; // how long token should be valid

    public String generateToken(String username, Set<String> roles) {
        // storing roles as comma string
        String rolesCsv = String.join(",", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expMs); // TODO: maybe refresh token later

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesCsv)  // saving roles inside token
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired :(");
        } catch (Exception e) {
            System.out.println("Token is not valid, my bad maybe");
        }
        return false;
    }

    public String extractUsername(String token) {
        // return null if token is wrong (just safe check)
        try {
            return Jwts.parser().setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> extractRoles(String token) {
        try {
            String r = (String) Jwts.parser().setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles");

            if (r == null || r.isBlank()) {
                return Set.of(); // no roles
            }
            return new HashSet<>(Arrays.asList(r.split(",")));
        } catch (Exception e) {
            return Set.of(); // if something wrong just return empty
        }
    }
}
