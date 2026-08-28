package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PartRequest(
        @NotBlank(message = "Code is required")
        String code,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity should be greater than 0")
        Integer quantity,

        @NotNull(message = "Price is required")
        @Positive(message = "Price should be greater than 0")
        BigDecimal price
) {

}
