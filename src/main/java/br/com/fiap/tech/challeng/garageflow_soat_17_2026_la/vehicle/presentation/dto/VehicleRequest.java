package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record VehicleRequest(
        @NotBlank(message = "Plate is required")
        String plate,
        String brand,
        String model,
        Integer year
) {

}
