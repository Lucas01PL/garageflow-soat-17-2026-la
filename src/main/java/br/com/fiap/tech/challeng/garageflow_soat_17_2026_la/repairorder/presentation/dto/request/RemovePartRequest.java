package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RemovePartRequest {

    @Min(value = 1, message = "Quantity must be a positive integer")
    private Integer quantity;
}
