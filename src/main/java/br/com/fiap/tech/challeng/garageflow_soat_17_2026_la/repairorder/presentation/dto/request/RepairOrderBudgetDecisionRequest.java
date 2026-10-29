package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record RepairOrderBudgetDecisionRequest(
        @NotNull Boolean approved
) {
}
