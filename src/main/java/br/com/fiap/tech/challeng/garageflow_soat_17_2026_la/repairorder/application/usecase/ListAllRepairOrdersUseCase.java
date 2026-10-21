package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ListAllRepairOrdersUseCase {

    private RepairOrderRepository repository;

    public List<RepairOrder> execute() {
        return repository.listAll(List.of(RepairOrderStatus.IN_EXECUTION, RepairOrderStatus.AWAITING_APPROVAL, RepairOrderStatus.IN_DIAGNOSIS, RepairOrderStatus.RECEIVED));
    }
}

