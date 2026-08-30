package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.document.UserDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.mongo.UserMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMongoRepository userMongoRepository;

    public UserRepositoryImpl(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public User save(User user) {
        UserDocument entity = toEntity(user);
        UserDocument saved = userMongoRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsById(String id) {
        return userMongoRepository.existsById(id);
    }

    @Override
    public boolean existsByRole(UserRole role) {
        return userMongoRepository.existsByRole(role);
    }

    @Override
    public void deleteById(String id) {
        userMongoRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(String id) {
        return userMongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMongoRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findByFullNameContainingIgnoreCase(String fullName) {
        return userMongoRepository.findByFullNameContainingIgnoreCase(fullName)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private User toDomain(UserDocument entity) {
        User domain = new User();
        domain.setId(entity.getId());
        domain.setFullName(entity.getFullName());
        domain.setEmail(entity.getEmail());
        domain.setPassword(entity.getPassword());
        domain.setStatus(entity.getStatus());
        domain.setRole(entity.getRole());
        return domain;
    }

    private UserDocument toEntity(User domain) {
        UserDocument entity = new UserDocument();
        entity.setId(domain.getId());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setStatus(domain.getStatus());
        entity.setRole(domain.getRole());
        return entity;
    }
}

