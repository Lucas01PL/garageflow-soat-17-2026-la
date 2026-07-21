package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUserByFullNameUseCase {

    @Autowired
    private UserRepository repository;

    public List<User> execute(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        return repository.findByFullNameContainingIgnoreCase(fullName);
    }
}

