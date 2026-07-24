package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto;

import java.math.BigDecimal;

public class WorkshopServiceResponseDTO {

    private String id;
    private String description;
    private BigDecimal value;

    public WorkshopServiceResponseDTO() {
    }

    public WorkshopServiceResponseDTO(String id, String description, BigDecimal value) {
        this.id = id;
        this.description = description;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

