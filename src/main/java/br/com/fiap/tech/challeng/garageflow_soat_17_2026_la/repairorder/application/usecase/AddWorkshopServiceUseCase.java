package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddWorkshopServiceRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddWorkshopServiceUseCase {

    private final RepairOrderRepository repository;
    private final WorkshopServiceRepository workshopServiceRepository;
    private final RepairOrderFinder repairOrderFinder;

    public RepairOrder execute(
            String repairOrderId,
            AddWorkshopServiceRequest request) {

        RepairOrder repairOrder = repairOrderFinder.findById(repairOrderId);

        WorkshopService workshopService =
                getWorkshopService(request.getWorkshopServiceId());

        WorkshopServiceSnapshot snapshot =
                WorkshopServiceSnapshot.from(
                        workshopService,
                        request.getQuantity());

        repairOrder.addWorkshopService(snapshot);

        return repository.save(repairOrder);
    }

    private WorkshopService getWorkshopService(String workshopServiceId) {

        if (workshopServiceId == null || workshopServiceId.isBlank()) {
            throw new RequiredFieldException("workshopServiceId");
        }

        return workshopServiceRepository.findById(workshopServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workshop service", "id", workshopServiceId));
    }

}

