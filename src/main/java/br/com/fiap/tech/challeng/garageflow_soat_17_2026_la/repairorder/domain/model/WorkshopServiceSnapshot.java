package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            throw new InvalidFieldValueException("quantity",
                    "Workshop service quantity must be greater than zero.");

        WorkshopServiceSnapshot snapshot = new WorkshopServiceSnapshot();
        snapshot.setWorkshopServiceId(workshopService.getId());
        snapshot.setDescription(workshopService.getDescription());
        snapshot.setQuantity(quantity);
        snapshot.setUnitPrice(workshopService.getPrice());
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
        this.startedAt = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void finish(Integer durationInMinutes) {
        if (!isInExecution()) {
            throw new InvalidRepairOrderStateException("Workshop service not in execution");
        }

        this.status = WorkshopServiceStatus.FINISHED;
        this.durationInMinutes = durationInMinutes;
        this.finishedAt = LocalDateTime.now(ZoneId.systemDefault());
    }
}
