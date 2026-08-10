package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto;

public record VehicleRequest(
        String plate,
        String brand,
        String model,
        Integer year
) {

}
