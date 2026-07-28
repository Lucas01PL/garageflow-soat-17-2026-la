package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteUserUseCase {

    private UserRepository repository;

    public void execute(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        repository.deleteById(id);
    }
}

