package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartStockOperationException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
class CancelRepairOrderUseCaseTest {

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @Mock
    private RepairOrderRepository repairOrderRepository;

    @Mock
    private PartStockControlUseCase partStockControlUseCase;

    @InjectMocks
    private CancelRepairOrderUseCase useCase;

    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {

        PartSnapshot partSnapshot =
                new PartSnapshot();

        partSnapshot.setId("part-1");
        partSnapshot.setCode("PART-001");
        partSnapshot.setDescription("Filtro de óleo");
        partSnapshot.setQuantity(2);
        partSnapshot.setUnitPrice(
                new BigDecimal("50.00")
        );

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .parts(
                        new ArrayList<>(
                                List.of(partSnapshot)
                        )
                )
                .build();
    }

    @Test
    void shouldCancelRepairOrderSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partStockControlUseCase.addPartStock(
                "part-1",
                2
        )).thenReturn("Stock updated");

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertNotNull(result);

        assertSame(
                repairOrder,
                result
        );

        assertEquals(
                RepairOrderStatus.CANCELLED,
                result.getStatus()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(partStockControlUseCase)
                .addPartStock("part-1", 2);

        verify(repairOrderRepository)
                .save(repairOrder);
    }

    @Test
    void shouldCancelRepairOrderWithoutParts() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertEquals(
                RepairOrderStatus.CANCELLED,
                result.getStatus()
        );

        verify(repairOrderRepository)
                .save(repairOrder);

        verifyNoInteractions(partStockControlUseCase);
    }

    @Test
    void shouldCancelRepairOrderWhenReceived() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertEquals(
                RepairOrderStatus.CANCELLED,
                result.getStatus()
        );

        verify(repairOrderRepository)
                .save(repairOrder);
    }

    @Test
    void shouldCancelRepairOrderWhenInDiagnosis() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertEquals(
                RepairOrderStatus.CANCELLED,
                result.getStatus()
        );
    }

    @Test
    void shouldCancelRepairOrderWhenApproved() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.APPROVED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertEquals(
                RepairOrderStatus.CANCELLED,
                result.getStatus()
        );
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

        verifyNoInteractions(
                partStockControlUseCase,
                repairOrderRepository
        );
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

        verifyNoInteractions(
                partStockControlUseCase,
                repairOrderRepository
        );
    }

    @Test
    void shouldThrowWhenRepairOrderIsInExecution() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_EXECUTION)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order cannot be cancelled in the current state.",
                exception.getMessage()
        );

        verify(partStockControlUseCase, never())
                .addPartStock(any(), any());

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsFinished() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.FINISHED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute("repair-order-1")
        );

        verify(partStockControlUseCase, never())
                .addPartStock(any(), any());

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsDelivered() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.DELIVERED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute("repair-order-1")
        );

        verify(partStockControlUseCase, never())
                .addPartStock(any(), any());

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsRejected() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.REJECTED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute("repair-order-1")
        );

        verify(partStockControlUseCase, never())
                .addPartStock(any(), any());

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsAlreadyCancelled() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.CANCELLED)
                .parts(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute("repair-order-1")
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenAddingPartBackToStockFails() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partStockControlUseCase.addPartStock(
                "part-1",
                2
        )).thenThrow(
                new RuntimeException("Stock unavailable")
        );

        PartStockOperationException exception =
                assertThrows(
                        PartStockOperationException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Failed to add part stock: Stock unavailable",
                exception.getMessage()
        );

        verify(partStockControlUseCase)
                .addPartStock("part-1", 2);

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));

        assertEquals(
                RepairOrderStatus.AWAITING_APPROVAL,
                repairOrder.getStatus()
        );
    }

    @Test
    void shouldReturnAllPartsToStockBeforeSavingRepairOrder() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partStockControlUseCase.addPartStock(
                "part-1",
                2
        )).thenReturn("Stock updated");

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        useCase.execute("repair-order-1");

        verify(partStockControlUseCase)
                .addPartStock("part-1", 2);

        verify(repairOrderRepository)
                .save(repairOrder);
    }
}