package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto;

import java.math.BigDecimal;

public record PartResponse(
        String id,
        String code,
        String name,
        Integer quantity,
        BigDecimal price
) {
}
