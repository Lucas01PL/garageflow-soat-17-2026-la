package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.PartService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderPartStockCoordinator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.AddPartCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidPartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddPartUseCase {

    private final RepairOrderRepository repository;
    private final PartService partService;
    private final RepairOrderFinder repairOrderFinder;
    private final RepairOrderPartStockCoordinator partStockCoordinator;

    public RepairOrder execute(String repairOrderId, AddPartCommand command) {

        RepairOrder repairOrder = repairOrderFinder.findById(repairOrderId);

        validatePartId(command.partId());
        PartSnapshot partSnapshot = partService.resolvePart(command.partId(), command.quantity());

        partStockCoordinator.debitAndExecute(
                partSnapshot,
                () -> {
                    repairOrder.addPart(partSnapshot);
                    return null;
                });

        return repository.save(repairOrder);
    }

    private void validatePartId(String partId) {
        if (partId == null || partId.isBlank()) {
            throw new InvalidPartException("Part ID cannot be empty");
        }
    }
}
