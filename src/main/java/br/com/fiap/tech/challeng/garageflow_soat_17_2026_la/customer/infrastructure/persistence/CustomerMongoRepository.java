package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerDocument, String> {
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
    Optional<CustomerDocument> findByDocument(String document);
    Optional<CustomerDocument> findByEmail(String email);
}
