package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// TODO: utilizar record ao inves de classe
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WorkshopServiceResponseDTO {

    private String id;
    private String description;
    private BigDecimal price;

}

