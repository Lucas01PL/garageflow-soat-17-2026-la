package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@NoArgsConstructor
@Data
public class WorkshopServiceSnapshot {
    private String workshopServiceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer durationInMinutes;
    private WorkshopServiceStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public static WorkshopServiceSnapshot from(WorkshopService workshopService, Integer quantity) {
        Objects.requireNonNull(workshopService);
        Objects.requireNonNull(quantity);

        if (quantity <= 0)
            throw new IllegalArgumentException(
                    "Workshop service quantity must be greater than zero.");

        WorkshopServiceSnapshot snapshot = new WorkshopServiceSnapshot();
        snapshot.setWorkshopServiceId(workshopService.getId());
        snapshot.setDescription(workshopService.getDescription());
        snapshot.setQuantity(quantity);
        snapshot.setUnitPrice(workshopService.getValue());
        snapshot.setStatus(WorkshopServiceStatus.WAITING_ATTENDING);
        return snapshot;
    }

    public boolean isWaitingAttending() {
        return WorkshopServiceStatus.WAITING_ATTENDING == this.status;
    }

    public boolean isInExecution() {
        return WorkshopServiceStatus.IN_EXECUTION == this.status;
    }

    public void start() {
        this.status = WorkshopServiceStatus.IN_EXECUTION;
        this.startedAt = LocalDateTime.now();
    }

    public void finish(Integer durationInMinutes) {
        this.status = WorkshopServiceStatus.FINISHED;
        this.durationInMinutes = durationInMinutes;
        this.finishedAt = LocalDateTime.now();
    }
}
