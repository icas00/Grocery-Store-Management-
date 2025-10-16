package grocery.service;

import grocery.entity.User;
import grocery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SimpleUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String password, boolean isAdmin){
        if(userRepository.existsByUsername(username)) throw new RuntimeException("user exists");
        User u = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(isAdmin ? Set.of("ROLE_ADMIN","ROLE_USER") : Set.of("ROLE_USER"))
                .build();
        return userRepository.save(u);
    }
}
