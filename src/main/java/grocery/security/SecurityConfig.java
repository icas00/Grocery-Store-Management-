package grocery.security;

import grocery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthFilter jwtAuthFilter; // filter which reads token

    @Bean
    public PasswordEncoder passwordEncoder() {
        // just using BCrypt for now safest and easy
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        //  used in auth controller to check login password
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable(); // TODO: enable later if we learn how 😅

        // allow products to be shown publicly
        http.authorizeHttpRequests(auth -> auth
                //login/register must be open to all users
                .requestMatchers("/auth/**").permitAll()

                .requestMatchers("/products/**").permitAll()

                // everything else needs login for now
                .anyRequest().authenticated()
        );

        // to show H2 in browser
        http.headers().frameOptions().disable();

        // adding JWT check filter before default one
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
