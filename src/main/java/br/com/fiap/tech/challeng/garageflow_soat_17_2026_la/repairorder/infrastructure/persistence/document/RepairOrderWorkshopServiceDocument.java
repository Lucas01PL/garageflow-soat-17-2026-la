package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepairOrderWorkshopServiceDocument {
    private String id;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer durationInMinutes;
}
