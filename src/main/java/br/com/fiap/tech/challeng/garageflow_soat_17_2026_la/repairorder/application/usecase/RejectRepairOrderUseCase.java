package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartStockOperationException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RejectRepairOrderUseCase {

    private final RepairOrderFinder repairOrderFinder;
    private final RepairOrderRepository repairOrderRepository;
    private final PartStockControlUseCase partStockControlUseCase;

    @Transactional
    public RepairOrder execute(String repairOrderId) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

        try {
            for (var partSnapshot : repairOrder.getParts()) {
                partStockControlUseCase.addPartStock(partSnapshot.getId(), partSnapshot.getQuantity());
            }
        } catch (Exception e) {
            throw new PartStockOperationException("add", e.getMessage());
        }

        repairOrder.reject();

        return repairOrderRepository.save(repairOrder);
    }
}