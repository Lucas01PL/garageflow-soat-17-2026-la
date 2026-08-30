package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListRepairOrdersByStatusUseCase {

    private final RepairOrderRepository repository;

    public List<RepairOrder> execute(String status) {

        if (status == null || status.isBlank()) {
            throw new RequiredFieldException("status");
        }

        return repository.findByStatusContainingIgnoreCase(status);
    }
}
