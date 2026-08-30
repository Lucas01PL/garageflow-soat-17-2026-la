package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UpdateUserUseCase {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    public User execute(String id, User update) {
        if (id == null || id.isBlank()) {
            throw new RequiredFieldException("id");
        }
        User existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (update.getFullName() != null && !update.getFullName().isBlank()) {
            existing.setFullName(update.getFullName());
        }
        if (update.getEmail() != null && !update.getEmail().isBlank()) {
            if (!isValidEmail(update.getEmail())) {
                throw new InvalidFieldValueException("email", "Email format is invalid");
            }
            existing.setEmail(update.getEmail());
        }
        if (update.getPassword() != null && !update.getPassword().isBlank()) {
            if (update.getPassword().length() < 6) {
                throw new InvalidFieldValueException("password", "Password must be at least 6 characters");
            }
            existing.setPassword(passwordEncoder.encode(update.getPassword()));
        }
        if (update.getStatus() != null && !update.getStatus().isBlank()) {
            existing.setStatus(update.getStatus());
        }
        if (update.getRole() != null) {
            existing.setRole(update.getRole());
        }


        return repository.save(existing);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}

