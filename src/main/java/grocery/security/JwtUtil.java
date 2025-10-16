package grocery.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration-ms}") private long expMs;

    public String generateToken(String username, Set<String> roles){
        String rolesCsv = String.join(",", roles);
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesCsv)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMs))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public boolean validate(String token){
        try {
            Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException ex){ return false; }
    }

    public String extractUsername(String token){
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public Set<String> extractRoles(String token){
        String r = (String)Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().get("roles");
        if(r==null || r.isBlank()) return Set.of();
        return new HashSet<>(Arrays.asList(r.split(",")));
    }
}
