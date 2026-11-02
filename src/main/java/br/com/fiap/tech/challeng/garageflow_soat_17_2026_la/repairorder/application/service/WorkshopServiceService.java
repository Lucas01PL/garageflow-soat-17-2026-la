package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WorkshopServiceService {

    private final WorkshopServiceRepository workshopServiceRepository;

    public WorkshopServiceSnapshot resolveWorkshopService(String workshopServiceId, Integer quantity) {
        if (workshopServiceId == null || workshopServiceId.isBlank()) {
            throw new RequiredFieldException("workshopServiceId");
        }

        WorkshopService workshopService = workshopServiceRepository.findById(workshopServiceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workshop service", "id", workshopServiceId));
        return WorkshopServiceSnapshot.from(workshopService, quantity);
    }
}
