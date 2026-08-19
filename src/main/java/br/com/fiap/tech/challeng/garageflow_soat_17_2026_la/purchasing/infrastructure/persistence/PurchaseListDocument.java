package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "purchase_lists")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseListDocument {

    @Id
    private String id;

    private LocalDateTime generatedAt;
    private PurchaseListStatus status;
    private List<PurchaseListItemDocument> items = new ArrayList<>();
    private String approvedBy;
    private LocalDateTime approvedAt;
}
