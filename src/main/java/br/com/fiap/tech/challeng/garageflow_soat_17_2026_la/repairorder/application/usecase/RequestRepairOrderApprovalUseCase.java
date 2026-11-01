package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.ApprovalTokenGenerator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RequestRepairOrderApprovalResult;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RequestRepairOrderApprovalUseCase {

    private static final Duration TOKEN_VALIDITY =
            Duration.ofDays(7);

    private final RepairOrderFinder repairOrderFinder;
    private final RepairOrderRepository repairOrderRepository;
    private final ApprovalTokenGenerator tokenGenerator;

    public RepairOrder execute(String repairOrderId) {
        return executeWithToken(repairOrderId).repairOrder();
    }

    public RequestRepairOrderApprovalResult executeWithToken(
            String repairOrderId) {

        RepairOrder repairOrder =
                repairOrderFinder.findById(repairOrderId);

        repairOrder.requestApproval();

        Instant now = Instant.now();
        String rawToken = tokenGenerator.generateToken();
        String tokenHash = tokenGenerator.hash(rawToken);

        repairOrder.createApprovalLink(
                tokenHash,
                now,
                now.plus(TOKEN_VALIDITY)
        );

        RepairOrder savedRepairOrder =
                repairOrderRepository.save(repairOrder);

        return new RequestRepairOrderApprovalResult(
                savedRepairOrder,
                rawToken
        );
    }
}