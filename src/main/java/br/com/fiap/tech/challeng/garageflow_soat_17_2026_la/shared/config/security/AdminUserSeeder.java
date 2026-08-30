package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public AdminUserSeeder(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${admin.seed.email:admin@garageflow.com}") String adminEmail,
                            @Value("${admin.seed.password:admin123}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        User admin = new User("Admin", adminEmail, passwordEncoder.encode(adminPassword), "ACTIVE", UserRole.ADMIN);
        userRepository.save(admin);

        log.info("[SEED] - No ADMIN user found. Created default admin with email: {}", adminEmail);
    }
}
