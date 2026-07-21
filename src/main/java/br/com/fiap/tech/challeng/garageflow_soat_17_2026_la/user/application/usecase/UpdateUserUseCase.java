package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UpdateUserUseCase {

    private UserRepository repository;

    public Optional<User> execute(String id, User update) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        Optional<User> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        User user = existing.get();
        
        if (update.getFullName() != null && !update.getFullName().isBlank()) {
            user.setFullName(update.getFullName());
        }
        if (update.getEmail() != null && !update.getEmail().isBlank()) {
            if (!isValidEmail(update.getEmail())) {
                throw new IllegalArgumentException("Email format is invalid");
            }
            user.setEmail(update.getEmail());
        }
        if (update.getPassword() != null && !update.getPassword().isBlank()) {
            if (update.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }
            user.setPassword(update.getPassword());
        }
        if (update.getStatus() != null && !update.getStatus().isBlank()) {
            user.setStatus(update.getStatus());
        }
        
        return Optional.of(repository.save(user));
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}

