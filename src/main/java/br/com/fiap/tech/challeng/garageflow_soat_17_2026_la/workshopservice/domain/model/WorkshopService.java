package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class WorkshopService {

    private String id;
    private String description;
    private BigDecimal value;

    public WorkshopService(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

}
