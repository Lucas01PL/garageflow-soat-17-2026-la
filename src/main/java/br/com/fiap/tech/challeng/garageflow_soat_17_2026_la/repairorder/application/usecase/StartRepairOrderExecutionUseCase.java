package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartRepairOrderExecutionUseCase {

    private final RepairOrderFinder repairOrderFinder;
    private final RepairOrderRepository repository;

    public RepairOrder execute(String repairOrderId) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

        repairOrder.startExecution();

        return repository.save(repairOrder);
    }
}