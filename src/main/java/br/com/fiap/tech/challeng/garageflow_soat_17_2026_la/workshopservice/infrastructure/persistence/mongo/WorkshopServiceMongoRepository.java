package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.mongo;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.document.WorkshopServiceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkshopServiceMongoRepository extends MongoRepository<WorkshopServiceDocument, String> {
    List<WorkshopServiceDocument> findByDescriptionContainingIgnoreCase(String description);
}

