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
class DeliverRepairOrderUseCaseTest {

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private DeliverRepairOrderUseCase useCase;

    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.FINISHED)
                .build();
    }

    @Test
    void shouldDeliverRepairOrderSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertNotNull(result);
        assertSame(repairOrder, result);

        assertEquals(
                RepairOrderStatus.DELIVERED,
                result.getStatus()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repository)
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
                        () -> useCase.execute(null)
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById(null);

        verifyNoInteractions(repository);
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
                        () -> useCase.execute("repair-order-1")
                );

        assertNotNull(exception);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenRepairOrderIsNotFinished() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_EXECUTION)
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order must be FINISHED to be delivered.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsAlreadyDelivered() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.DELIVERED)
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order must be FINISHED to be delivered.",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldNotPersistRepairOrderWhenDeliveryFails() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.APPROVED)
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute("repair-order-1")
        );

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterDelivery() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute("repair-order-1");

        ArgumentCaptor<RepairOrder> captor =
                ArgumentCaptor.forClass(RepairOrder.class);

        verify(repository)
                .save(captor.capture());

        RepairOrder captured =
                captor.getValue();

        assertSame(
                repairOrder,
                captured
        );

        assertEquals(
                RepairOrderStatus.DELIVERED,
                captured.getStatus()
        );
    }
}