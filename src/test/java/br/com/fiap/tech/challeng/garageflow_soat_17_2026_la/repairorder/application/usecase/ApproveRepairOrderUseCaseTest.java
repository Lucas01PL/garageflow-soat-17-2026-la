package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApproveRepairOrderUseCaseTest {

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @Mock
    private RepairOrderRepository repairOrderRepository;

    @InjectMocks
    private ApproveRepairOrderUseCase useCase;

    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .userId("user-1")
                .build();
    }

    @Test
    void shouldApproveRepairOrderSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1", "user-1");

        assertNotNull(result);
        assertSame(repairOrder, result);

        assertEquals(
                RepairOrderStatus.APPROVED,
                result.getStatus()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository)
                .save(repairOrder);
    }

    @Test
    void shouldThrowWhenRepairOrderIdIsNull() {

        when(repairOrderFinder.findById(null))
                .thenThrow(
                        new RequiredFieldException("repairOrderId")
                );

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(null, "user-1")
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById(null);

        verifyNoInteractions(repairOrderRepository);
    }

    @Test
    void shouldThrowAccessDeniedWhenUserIsNotTheOwner() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                AccessDeniedException.class,
                () -> useCase.execute("repair-order-1", "another-user")
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowAccessDeniedWhenUserIdIsNull() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                AccessDeniedException.class,
                () -> useCase.execute("repair-order-1", null)
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderDoesNotExist() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Repair Order",
                                "id",
                                "repair-order-1"
                        )
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> useCase.execute("repair-order-1", "user-1")
                );

        assertNotNull(exception);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(repairOrderRepository);
    }

    @Test
    void shouldThrowWhenRepairOrderIsNotAwaitingApproval() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .userId("user-1")
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1", "user-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order must be awaiting customer approval.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldNotPersistRepairOrderWhenApprovalFails() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_EXECUTION)
                .userId("user-1")
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute("repair-order-1", "user-1")
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterApproval() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute("repair-order-1", "user-1");

        ArgumentCaptor<RepairOrder> captor =
                ArgumentCaptor.forClass(RepairOrder.class);

        verify(repairOrderRepository)
                .save(captor.capture());

        RepairOrder captured =
                captor.getValue();

        assertSame(
                repairOrder,
                captured
        );

        assertEquals(
                RepairOrderStatus.APPROVED,
                captured.getStatus()
        );
    }
}