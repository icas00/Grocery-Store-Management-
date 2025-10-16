package grocery.util;

import grocery.entity.User;
import grocery.entity.Product;
import grocery.repository.ProductRepository;
import grocery.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SampleDataLoader {

    private final ProductRepository repo;
    private final UserRepository userRepo;

    @PostConstruct
    public void load() {
        if (userRepo.count() == 0) {
            userRepo.save(User.builder()
                    .username("admin")
                    .password(new BCryptPasswordEncoder().encode("admin123"))
                    .roles(Set.of("ROLE_ADMIN", "ROLE_USER"))
                    .build());

            userRepo.save(User.builder()
                    .username("user")
                    .password(new BCryptPasswordEncoder().encode("user123"))
                    .roles(Set.of("ROLE_USER"))
                    .build());
        }

        if (repo.count() == 0) {
            repo.save(Product.builder()
                    .name("Sugar 1kg")
                    .description("Refined")
                    .price(45)
                    .quantity(20)
                    .category("Grocery")
                    .build());

            repo.save(Product.builder()
                    .name("Salt 1kg")
                    .description("Iodized")
                    .price(25)
                    .quantity(50)
                    .category("Grocery")
                    .build());
        }
    }
}
