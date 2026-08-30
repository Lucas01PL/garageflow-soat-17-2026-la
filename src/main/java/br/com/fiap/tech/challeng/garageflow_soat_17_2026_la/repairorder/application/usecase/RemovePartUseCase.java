package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidPartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartStockOperationException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.RemovePartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RemovePartUseCase {

    private final RepairOrderRepository repository;
    private final PartRepository partRepository;
    private final RepairOrderFinder repairOrderFinder;
    private final PartStockControlUseCase partStockControlUseCase;

    public RepairOrder execute(String repairOrderId, String partId, RemovePartRequest request) {

        RepairOrder repairOrder = repairOrderFinder.findById(repairOrderId);

        Part part = getPart(partId);

        PartSnapshot partSnapshot = PartSnapshot.from(part, request.getQuantity());

        try {
            partStockControlUseCase.addPartStock(partId, request.getQuantity());
        } catch (Exception e) {
            throw new PartStockOperationException("add", e.getMessage());
        }

        try {
            repairOrder.removePart(partSnapshot);
        } catch (Exception e) {
            try {
                partStockControlUseCase.debitPartStock(partId, request.getQuantity());
            } catch (Exception rollbackException) {
                throw new PartStockOperationException("remove", "Failed to rollback stock after remove part failure: " + rollbackException.getMessage());
            }
            throw e;
        }

        return repository.save(repairOrder);
    }

    private Part getPart(String partId) {
        if (partId == null || partId.isBlank()) {
            throw new InvalidPartException("Part ID cannot be empty");
        }

        return partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part", "id", partId));

    }
}

