package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.WorkshopServiceResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkshopServiceResponseMapper {

    public WorkshopServiceResponse toResponse(WorkshopServiceSnapshot model) {
        if (model == null) return null;
        var workshopServiceResponse = new WorkshopServiceResponse();
        workshopServiceResponse.setWorkshopServiceId(model.getWorkshopServiceId());
        workshopServiceResponse.setDescription(model.getDescription());
        workshopServiceResponse.setQuantity(model.getQuantity());
        workshopServiceResponse.setUnitPrice(model.getUnitPrice());
        workshopServiceResponse.setDurationInMinutes(model.getDurationInMinutes());
        workshopServiceResponse.setStatus(model.getStatus().getDescription());
        workshopServiceResponse.setStartedAt(model.getStartedAt());
        workshopServiceResponse.setFinishedAt(model.getFinishedAt());
        return workshopServiceResponse;
    }
}

