package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepairOrderFinder {

    private final RepairOrderRepository repository;

    public RepairOrder findById(String repairOrderId) {

        if (repairOrderId == null || repairOrderId.isBlank()) {
            throw new RequiredFieldException("repairOrderId");
        }

        return repository.findById(repairOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Repair Order",
                                "id",
                                repairOrderId));
    }
}