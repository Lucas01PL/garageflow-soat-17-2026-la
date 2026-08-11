package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Data
public class WorkshopServiceSnapshot {
    private String workshopServiceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer durationInMinutes;

    public WorkshopServiceSnapshot(String workshopServiceId, Integer quantity, Integer durationInMinutes) {
        this.workshopServiceId = workshopServiceId;
        this.quantity = quantity;
        this.durationInMinutes = durationInMinutes;
    }

    public static WorkshopServiceSnapshot from(WorkshopService workshopService, Integer quantity) {
        Objects.requireNonNull(workshopService);
        Objects.requireNonNull(quantity);

        if (quantity < 0)
            throw new IllegalArgumentException(
                    "Workshop service quantity must be greater than zero.");

        WorkshopServiceSnapshot snapshot = new WorkshopServiceSnapshot();
        snapshot.setWorkshopServiceId(workshopService.getId());
        snapshot.setDescription(workshopService.getDescription());
        snapshot.setQuantity(quantity);
        snapshot.setUnitPrice(workshopService.getValue());
        return snapshot;
    }
}
