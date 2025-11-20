package grocery.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter that intercepts requests to authenticate users based on a JWT.
 * Runs once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    // Checks for a JWT in the Authorization header and authenticates the user if valid.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get the Authorization header.
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String jwt = null;

        // Check if the header is present and starts with "Bearer ".
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            // Validate the token and get the username.
            if (jwtUtil.validateToken(jwt)) {
                username = jwtUtil.getUsernameFromJwt(jwt);
            }
        }

        // If we have a username and the user is not already authenticated...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // ...load the user details...
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Extract roles from JWT and convert to GrantedAuthority
            List<String> roles = jwtUtil.getRolesFromJwt(jwt);
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                authorities = roles.stream()
                                   .map(SimpleGrantedAuthority::new)
                                   .collect(Collectors.toList());
            }

            // ...create an authentication token with authorities...
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // ...and set it in the security context.
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Continue the filter chain.
        filterChain.doFilter(request, response);
    }
}
