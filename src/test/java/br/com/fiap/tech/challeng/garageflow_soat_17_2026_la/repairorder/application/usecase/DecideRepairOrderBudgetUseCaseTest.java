package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.ApprovalTokenGenerator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrderApprovalLink;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecideRepairOrderBudgetUseCaseTest {

    private static final String RAW_TOKEN = "raw-token";
    private static final String TOKEN_HASH = "token-hash";

    @Mock
    private RepairOrderRepository repairOrderRepository;
    @Mock
    private ApprovalTokenGenerator tokenGenerator;
    @Mock
    private RejectRepairOrderUseCase rejectRepairOrderUseCase;

    @InjectMocks
    private DecideRepairOrderBudgetUseCase useCase;

    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .approvalLink(new RepairOrderApprovalLink(
                        TOKEN_HASH,
                        now.minusSeconds(1),
                        now.plusSeconds(60),
                        null
                ))
                .build();

        when(tokenGenerator.hash(RAW_TOKEN)).thenReturn(TOKEN_HASH);
        when(repairOrderRepository.findByApprovalTokenHash(TOKEN_HASH))
                .thenReturn(Optional.of(repairOrder));
        Mockito.lenient().when(repairOrderRepository.save(any(RepairOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldApproveAndInvalidateToken() {
        RepairOrder result = useCase.execute(RAW_TOKEN, true);

        assertEquals(RepairOrderStatus.APPROVED, result.getStatus());
        assertNull(result.getApprovalLink());
        verify(repairOrderRepository).save(repairOrder);
        verify(rejectRepairOrderUseCase, never())
                .execute(any(RepairOrder.class));
    }

    @Test
    void shouldRejectThroughExistingStockRestorationFlow() {
        when(rejectRepairOrderUseCase.execute(repairOrder))
                .thenAnswer(invocation -> {
                    repairOrder.reject();
                    return repairOrder;
                });

        RepairOrder result = useCase.execute(RAW_TOKEN, false);

        assertEquals(RepairOrderStatus.REJECTED, result.getStatus());
        assertNull(result.getApprovalLink());
        verify(rejectRepairOrderUseCase)
                .execute(repairOrder);
        verify(repairOrderRepository, never()).save(any(RepairOrder.class));
    }

    @Test
    void shouldRejectMissingOrUnknownToken() {
        assertThrows(BadCredentialsException.class, () -> useCase.execute(" ", true));

        when(repairOrderRepository.findByApprovalTokenHash(TOKEN_HASH))
                .thenReturn(Optional.empty());

        assertThrows(
                BadCredentialsException.class,
                () -> useCase.execute(RAW_TOKEN, true)
        );
    }

    @Test
    void shouldRejectExpiredToken() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .approvalLink(new RepairOrderApprovalLink(
                        TOKEN_HASH,
                        Instant.now().minusSeconds(120),
                        Instant.now().minusSeconds(60),
                        null
                ))
                .build();
        when(repairOrderRepository.findByApprovalTokenHash(TOKEN_HASH))
                .thenReturn(Optional.of(repairOrder));

        assertThrows(
                BadCredentialsException.class,
                () -> useCase.execute(RAW_TOKEN, true)
        );

        verify(repairOrderRepository, never()).save(any(RepairOrder.class));
    }

    @Test
    void shouldRejectRepairOrderOutsideApprovalState() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .approvalLink(new RepairOrderApprovalLink(
                        TOKEN_HASH,
                        Instant.now().minusSeconds(1),
                        Instant.now().plusSeconds(60),
                        null
                ))
                .build();
        when(repairOrderRepository.findByApprovalTokenHash(TOKEN_HASH))
                .thenReturn(Optional.of(repairOrder));

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute(RAW_TOKEN, true)
        );

        verify(repairOrderRepository, never()).save(any(RepairOrder.class));
        verify(rejectRepairOrderUseCase, never())
                .execute(any(RepairOrder.class));
    }
}
