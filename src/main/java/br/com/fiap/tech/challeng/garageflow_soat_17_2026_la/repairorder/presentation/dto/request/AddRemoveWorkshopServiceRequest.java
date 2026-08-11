package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddRemoveWorkshopServiceRequest {
    @NotBlank(message = "Workshop Service ID cannot be blank")
    private String workshopServiceId;

    @Min(value = 1, message = "Quantity must be a positive integer")
    private Integer quantity;
}