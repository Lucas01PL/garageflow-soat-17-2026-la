package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateRepairOrderRequest {

    @NotBlank(message = "Vehicle ID cannot be blank")
    private String vehicleId;

    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;
}

