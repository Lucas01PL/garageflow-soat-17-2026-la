package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateUserUseCase {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    public User execute(User user) {
        if (user == null) {
            throw new RequiredObjectException("User cannot be null");
        }
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new RequiredFieldException("fullName");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RequiredFieldException("email");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new InvalidFieldValueException("email", "Email format is invalid");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RequiredFieldException("password");
        }
        if (user.getPassword().length() < 6) {
            throw new InvalidFieldValueException("password", "Password must be at least 6 characters");
        }
        if (user.getStatus() == null || user.getStatus().isBlank()) {
            throw new RequiredFieldException("status");
        }
        if (user.getRole() == null) {
            throw new InvalidFieldValueException("role", "Role cannot be empty");
        }

        // Check if email already exists
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new InvalidFieldValueException("email", "Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}

