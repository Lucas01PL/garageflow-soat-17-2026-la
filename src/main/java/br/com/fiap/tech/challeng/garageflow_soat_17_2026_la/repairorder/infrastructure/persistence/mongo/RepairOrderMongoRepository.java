package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.mongo;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairOrderMongoRepository extends MongoRepository<RepairOrderDocument, String> {
    List<RepairOrderDocument> findByStatusContainingIgnoreCase(String status);

    List<RepairOrderDocument> findByCustomer_CustomerId(String customerId);
}

