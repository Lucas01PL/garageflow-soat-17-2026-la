package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.FinishWorkshopServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinishWorkshopServiceUseCase {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderFinder repairOrderFinder;

    public RepairOrder execute(String repairOrderId, String workshopServiceId, FinishWorkshopServiceRequest request) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

        var workshopService = repairOrder.getWorkshopServices()
                .stream()
                .filter(ws -> ws.getWorkshopServiceId().equals(workshopServiceId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Workshop service not found"));

        if (!workshopService.isInExecution())
            throw new IllegalArgumentException("Workshop service not in execution");

        workshopService.setDurationInMinutes(request.getDurationInMinutes());
        workshopService.setFinishedAt(LocalDateTime.now());
        workshopService.setStatus(WorkshopServiceStatus.FINISHED);

        return repairOrderRepository.save(repairOrder);
    }
}