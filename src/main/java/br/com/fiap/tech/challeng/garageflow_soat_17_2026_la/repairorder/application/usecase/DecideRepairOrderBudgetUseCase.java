package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.ApprovalTokenGenerator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DecideRepairOrderBudgetUseCase {

    private static final String INVALID_TOKEN_MESSAGE =
            "Approval token is invalid, expired or already used.";

    private final RepairOrderRepository repairOrderRepository;
    private final ApprovalTokenGenerator tokenGenerator;
    private final RejectRepairOrderUseCase rejectRepairOrderUseCase;

    @Transactional
    public RepairOrder execute(String token, boolean approved) {
        if (token == null || token.isBlank()) {
            throw new BadCredentialsException(INVALID_TOKEN_MESSAGE);
        }

        String tokenHash = tokenGenerator.hash(token);
        RepairOrder repairOrder = repairOrderRepository
                .findByApprovalTokenHash(tokenHash)
                .orElseThrow(() -> new BadCredentialsException(INVALID_TOKEN_MESSAGE));

        if (repairOrder.getStatus() != RepairOrderStatus.AWAITING_APPROVAL) {
            throw new InvalidRepairOrderStateException(
                    "Repair Order must be awaiting customer approval.");
        }

        try {
            repairOrder.consumeApprovalLink(Instant.now());
        } catch (InvalidRepairOrderStateException exception) {
            throw new BadCredentialsException(INVALID_TOKEN_MESSAGE, exception);
        }

        if (!approved) {
            return rejectRepairOrderUseCase.execute(repairOrder);
        }

        repairOrder.approve();
        return repairOrderRepository.save(repairOrder);
    }
}
