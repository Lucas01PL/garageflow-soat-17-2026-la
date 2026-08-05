package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.mongo;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.infrastructure.persistence.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(String email);
    List<UserDocument> findByFullNameContainingIgnoreCase(String fullName);
}

