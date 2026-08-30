package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartMongoRepository extends MongoRepository<PartDocument, String> {
    boolean existsByCode(String code);
    Optional<PartDocument> findByCode(String code);
    List<PartDocument> findByQuantityLessThanEqual(Integer quantity);
}
