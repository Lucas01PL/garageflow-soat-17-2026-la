package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateWorkshopServiceRequestDTO {

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    private String description;

    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

}

