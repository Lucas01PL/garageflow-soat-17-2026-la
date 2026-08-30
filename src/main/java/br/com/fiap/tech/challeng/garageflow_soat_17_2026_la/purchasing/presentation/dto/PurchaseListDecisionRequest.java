package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record PurchaseListDecisionRequest(
        @NotBlank(message = "userId cannot be blank")
        String userId
) {
}
