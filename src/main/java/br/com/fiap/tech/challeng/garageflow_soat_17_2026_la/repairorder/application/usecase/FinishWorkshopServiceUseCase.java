package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.FinishWorkshopServiceCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FinishWorkshopServiceUseCase {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairOrderFinder repairOrderFinder;

    public RepairOrder execute(String repairOrderId, String workshopServiceId, FinishWorkshopServiceCommand command) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

       repairOrder.finishWorkshopService(workshopServiceId, command.durationInMinutes());

        return repairOrderRepository.save(repairOrder);
    }
}