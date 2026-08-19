package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.CreateWorkshopServiceRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.WorkshopServiceResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkshopServiceMapper {

    public WorkshopService toModel(CreateWorkshopServiceRequestDTO dto) {
        if (dto == null) return null;
        return new WorkshopService(dto.getDescription(), dto.getValue());
    }

    public WorkshopServiceResponseDTO toResponse(WorkshopService model) {
        if (model == null) return null;
        return new WorkshopServiceResponseDTO(
                model.getId(),
                model.getDescription(),
                model.getPrice()
        );
    }
}

