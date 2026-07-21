package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserByIdUseCase {

    @Autowired
    private UserRepository repository;

    public Optional<User> execute(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        return repository.findById(id);
    }
}

