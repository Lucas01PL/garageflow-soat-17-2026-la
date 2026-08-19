package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseListMongoRepository extends MongoRepository<PurchaseListDocument, String> {
    List<PurchaseListDocument> findByStatus(PurchaseListStatus status);
}
