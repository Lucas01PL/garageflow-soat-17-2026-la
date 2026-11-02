package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.application.event;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.domain.type.RepairOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record RepairOrderStatusChangedEvent(
        String repairOrderId,
        String repairOrderNumber,
        String customerName,
        String customerEmail,
        RepairOrderStatus newStatus,
        Instant occurredAt,
        String approvalToken,
        List<BudgetItem> services,
        List<BudgetItem> parts,
        BigDecimal totalServices,
        BigDecimal totalParts,
        BigDecimal total
) {
    public RepairOrderStatusChangedEvent {
        services = services == null ? List.of() : List.copyOf(services);
        parts = parts == null ? List.of() : List.copyOf(parts);
    }

    public RepairOrderStatusChangedEvent(
            String repairOrderId,
            String repairOrderNumber,
            String customerName,
            String customerEmail,
            RepairOrderStatus newStatus,
            Instant occurredAt,
            String approvalToken) {
        this(
                repairOrderId,
                repairOrderNumber,
                customerName,
                customerEmail,
                newStatus,
                occurredAt,
                approvalToken,
                List.of(),
                List.of(),
                null,
                null,
                null
        );
    }

    public RepairOrderStatusChangedEvent(
            String repairOrderId,
            String repairOrderNumber,
            String customerName,
            String customerEmail,
            RepairOrderStatus newStatus,
            Instant occurredAt) {
        this(
                repairOrderId,
                repairOrderNumber,
                customerName,
                customerEmail,
                newStatus,
                occurredAt,
                null,
                List.of(),
                List.of(),
                null,
                null,
                null
        );
    }

    public record BudgetItem(
            String description,
            Integer quantity,
            BigDecimal unitPrice
    ) {
    }
}