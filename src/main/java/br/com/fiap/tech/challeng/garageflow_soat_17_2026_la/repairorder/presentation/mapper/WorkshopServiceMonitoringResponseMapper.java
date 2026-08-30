package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.AverageWorkshopServiceExecutionTime;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.AverageWorkshopServiceExecutionTimeResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkshopServiceMonitoringResponseMapper {

    public AverageWorkshopServiceExecutionTimeResponse toResponse(
            AverageWorkshopServiceExecutionTime model) {

        return AverageWorkshopServiceExecutionTimeResponse.builder()
                .workshopServiceId(
                        model.getWorkshopServiceId()
                )
                .description(
                        model.getDescription()
                )
                .completedServices(
                        model.getCompletedServices()
                )
                .averageDurationInMinutes(
                        model.getAverageDurationInMinutes()
                )
                .minimumDurationInMinutes(
                        model.getMinimumDurationInMinutes()
                )
                .maximumDurationInMinutes(
                        model.getMaximumDurationInMinutes()
                )
                .build();
    }
}