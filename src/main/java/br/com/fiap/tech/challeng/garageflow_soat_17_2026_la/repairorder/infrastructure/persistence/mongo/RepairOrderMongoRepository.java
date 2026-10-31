package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.mongo;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairOrderMongoRepository extends MongoRepository<RepairOrderDocument, String> {
    List<RepairOrderDocument> findByStatusContainingIgnoreCaseOrderByStatusCodeAscCreatedDateAsc(String status);

    List<RepairOrderDocument> findByCustomer_CustomerId(String customerId);

    @Query(
            value = """
                    {
                      'status': { '$in': ?0 },
                      'parts.id': ?1
                    }
                    """,
            exists = true
    )
    boolean existsByPartIdAndStatusIn(
            List<RepairOrderStatus> statuses,
            String partId
    );

    List<RepairOrderDocument> findByStatusInOrderByStatusAscCreatedDateAsc(@Param("status") List<RepairOrderStatus> status);

    Optional<RepairOrderDocument> findByApprovalLink_TokenHash(String tokenHash);
}

