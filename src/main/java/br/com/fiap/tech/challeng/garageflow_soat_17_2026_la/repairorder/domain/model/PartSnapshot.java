package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.infrastructure.persistence.PartDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderPartDocument;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class PartSnapshot {
    private String id;
    private String code;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;

    public PartSnapshot(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static PartSnapshot from(Part part, Integer quantity) {

        if (part.getQuantity() <= 0) {
            throw new IllegalArgumentException("Part quantity must be greater than zero");
        }

        PartSnapshot snapshot = new PartSnapshot();
        snapshot.setId(part.getId());
        snapshot.setCode(part.getCode());
        snapshot.setDescription(part.getName());
        snapshot.setQuantity(quantity);
        snapshot.setUnitPrice(part.getPrice());
        return snapshot;
    }
}
