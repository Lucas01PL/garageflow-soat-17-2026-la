package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class RepairOrderWorkshopServiceDocument {
    private String workshopServiceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer durationInMinutes;
    private WorkshopServiceStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
