package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseListResponse(
        String id,
        LocalDateTime generatedAt,
        PurchaseListStatus status,
        List<PurchaseListItemResponse> items,
        String approvedBy,
        LocalDateTime approvedAt
) {
}
