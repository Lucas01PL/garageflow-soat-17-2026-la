package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StartWorkshopServiceUseCase {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderFinder repairOrderFinder;

    public RepairOrder execute(String repairOrderId, String workshopServiceId) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

        var workshopService = repairOrder.getWorkshopServices()
                .stream()
                .filter(ws -> ws.getWorkshopServiceId().equals(workshopServiceId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Workshop service not found"));

        if (!workshopService.isWaitingAttending())
            throw new IllegalArgumentException("Workshop service not waiting attending");

        workshopService.setStartedAt(LocalDateTime.now());
        workshopService.setStatus(WorkshopServiceStatus.IN_EXECUTION);

        return repairOrderRepository.save(repairOrder);
    }
}