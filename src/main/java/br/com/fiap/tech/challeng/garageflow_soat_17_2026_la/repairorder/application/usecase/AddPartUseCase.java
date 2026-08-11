package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddRemovePartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddPartUseCase {

    private final RepairOrderRepository repository;
    private final PartRepository partRepository;
    private final RepairOrderFinder repairOrderFinder;

    public RepairOrder execute(String repairOrderId, AddRemovePartRequest request) {

        RepairOrder repairOrder = repairOrderFinder.findById(repairOrderId);

        Part part = getPart(request.getPartId());

        PartSnapshot partSnapshot = PartSnapshot.from(part, request.getQuantity());

        repairOrder.addPart(partSnapshot);

        return repository.save(repairOrder);
    }

    private Part getPart(String partId) {
        if (partId == null || partId.isBlank()) {
            throw new IllegalArgumentException("Part ID cannot be empty");
        }

        return partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part", "id", partId));


    }
}

