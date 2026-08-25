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
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddRemovePartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.NotEnoughResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddPartUseCase {

    private final RepairOrderRepository repository;
    private final PartRepository partRepository;
    private final RepairOrderFinder repairOrderFinder;
    private final PartStockControlUseCase partStockControlUseCase;

    public RepairOrder execute(String repairOrderId, AddRemovePartRequest request) {

        RepairOrder repairOrder = repairOrderFinder.findById(repairOrderId);

        Part part = getPart(request.getPartId());

        PartSnapshot partSnapshot = PartSnapshot.from(part, request.getQuantity());

        try {
            partStockControlUseCase.debitPartStock(request.getPartId(), request.getQuantity());
        } catch (NotEnoughResourceException e) {
            throw e;
        } catch (Exception e) {
            throw new PartStockOperationException("debit", e.getMessage());
        }

        try {
            repairOrder.addPart(partSnapshot);
        } catch (Exception e) {
            try {
                partStockControlUseCase.addPartStock(request.getPartId(), request.getQuantity());
            } catch (Exception rollbackException) {
                throw new PartStockOperationException("add", "Failed to rollback stock after add part failure: " + rollbackException.getMessage());
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

