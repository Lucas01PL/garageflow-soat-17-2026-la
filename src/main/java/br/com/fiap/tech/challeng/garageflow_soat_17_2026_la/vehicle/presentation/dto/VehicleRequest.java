package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleRequest(
        @NotBlank(message = "Plate is required")
        String plate,

        @NotBlank(message = "Brand is required")
        String brand,

        @NotBlank(message = "Model is required")
        String model,

        @NotNull(message = "Year is required")
        @Positive(message = "Year must be a positive number")
        Integer year
) {

}
