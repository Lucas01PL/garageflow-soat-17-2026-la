package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientMongoRepository extends MongoRepository<ClientDocument, String> {
    boolean existsByDocument(String document);
    Optional<ClientDocument> findByDocument(String document);
}
