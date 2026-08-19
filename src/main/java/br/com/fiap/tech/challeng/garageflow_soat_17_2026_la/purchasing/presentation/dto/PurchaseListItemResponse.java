package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto;

import java.math.BigDecimal;

public record PurchaseListItemResponse(
        String partId,
        String partName,
        Integer currentQuantity,
        Integer quantityToBuy,
        BigDecimal unitPrice
) {
}
