package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApproveRepairOrderUseCase {

    private final RepairOrderFinder repairOrderFinder;
    private final RepairOrderRepository repairOrderRepository;

    public RepairOrder execute(String repairOrderId, String userId) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

        if (userId == null || !userId.equals(repairOrder.getUserId())) {
            throw new AccessDeniedException(
                    "Only the user who created this Repair Order can approve it.");
        }

        repairOrder.approve();

        return repairOrderRepository.save(repairOrder);
    }
}