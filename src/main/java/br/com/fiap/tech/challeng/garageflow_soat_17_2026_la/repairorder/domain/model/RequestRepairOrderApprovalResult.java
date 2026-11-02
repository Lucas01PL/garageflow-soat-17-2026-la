package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

public record RequestRepairOrderApprovalResult(
        RepairOrder repairOrder,
        String rawToken
) {
}