package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddWorkshopServiceRequest {
    @NotBlank(message = "Workshop Service ID cannot be blank")
    private String workshopServiceId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    @Min(value = 1, message = "Quantity must be a positive integer")
    private Integer quantity;
}