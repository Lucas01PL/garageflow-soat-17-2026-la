package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.application.event;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.domain.type.RepairOrderStatus;

import java.time.Instant;

public record RepairOrderStatusChangedEvent(
        String repairOrderId,
        String repairOrderNumber,
        String customerName,
        String customerEmail,
        RepairOrderStatus newStatus,
        Instant occurredAt
) {
}