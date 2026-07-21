package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

    @Autowired
    private UserRepository repository;

    public boolean execute(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}

