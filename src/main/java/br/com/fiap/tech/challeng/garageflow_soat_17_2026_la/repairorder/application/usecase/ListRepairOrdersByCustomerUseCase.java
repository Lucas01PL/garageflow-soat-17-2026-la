package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListRepairOrdersByCustomerUseCase {

    private final RepairOrderRepository repository;

    public List<RepairOrder> execute(String customerId) {

        if (customerId == null || customerId.isBlank()) {
            throw new RequiredFieldException("customerId");
        }

        return repository.findByCustomerId(customerId);
    }
}