package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    boolean existsById(String id);

    void deleteById(String id);

    Optional<User> findById(String id);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
}

