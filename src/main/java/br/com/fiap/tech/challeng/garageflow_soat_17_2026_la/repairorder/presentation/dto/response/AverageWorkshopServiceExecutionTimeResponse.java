package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AverageWorkshopServiceExecutionTimeResponse {

    private String workshopServiceId;

    private String description;

    private long completedServices;

    private BigDecimal averageDurationInMinutes;

    private Integer minimumDurationInMinutes;

    private Integer maximumDurationInMinutes;
}