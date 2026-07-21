package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreateWorkshopServiceRequestDTO {

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    private String description;

    @Positive(message = "Value must be greater than zero")
    private BigDecimal value;

    public CreateWorkshopServiceRequestDTO() {
    }

    public CreateWorkshopServiceRequestDTO(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

