package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.WorkshopServiceService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.RemoveWorkshopServiceCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RemoveWorkshopServiceUseCase {

    private final RepairOrderRepository repository;
    private final WorkshopServiceService itemResolver;
    private final RepairOrderFinder repairOrderFinder;

    public RepairOrder execute(
            String repairOrderId,
            String workshopServiceId,
            RemoveWorkshopServiceCommand command) {

        RepairOrder repairOrder = repairOrderFinder.findById(repairOrderId);

        WorkshopServiceSnapshot snapshot = itemResolver.resolveWorkshopService(
                workshopServiceId,
                command.quantity());

        repairOrder.removeWorkshopService(snapshot);

        return repository.save(repairOrder);
    }
}
