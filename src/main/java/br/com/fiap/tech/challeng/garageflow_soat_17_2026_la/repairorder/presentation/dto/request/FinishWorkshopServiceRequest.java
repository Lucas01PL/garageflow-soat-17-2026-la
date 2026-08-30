package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FinishWorkshopServiceRequest {
    @NotNull(message = "Duration in minutes is required")
    @Positive(message = "Duration in minutes must be a positive integer")
    @Min(value = 1, message = "Duration in minutes must be a positive integer")
    private Integer durationInMinutes;
}