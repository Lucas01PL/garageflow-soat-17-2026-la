package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderItemException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class RequestRepairOrderApprovalUseCaseTest {

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @Mock
    private RepairOrderRepository repairOrderRepository;

    @InjectMocks
    private RequestRepairOrderApprovalUseCase useCase;

    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {

        WorkshopServiceSnapshot workshopService =
                new WorkshopServiceSnapshot();

        workshopService.setWorkshopServiceId("workshop-service-1");
        workshopService.setDescription("Troca de óleo");
        workshopService.setQuantity(1);
        workshopService.setUnitPrice(new BigDecimal("150.00"));
        workshopService.setStatus(
                WorkshopServiceStatus.WAITING_ATTENDING
        );

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .workshopServices(
                        new ArrayList<>(
                                List.of(workshopService)
                        )
                )
                .build();
    }

    @Test
    void shouldRequestApprovalSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1");

        assertNotNull(result);
        assertSame(repairOrder, result);

        assertEquals(
                RepairOrderStatus.AWAITING_APPROVAL,
                result.getStatus()
        );

        assertNotNull(result.getUpdatedDate());

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
                        () -> useCase.execute(null)
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

        verifyNoInteractions(repairOrderRepository);
    }

    @Test
    void shouldThrowWhenRepairOrderIsNotInDiagnosis() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .workshopServices(repairOrder.getWorkshopServices())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order must be in diagnosis to request approval.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderHasNoWorkshopServices() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .workshopServices(new ArrayList<>())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderItemException exception =
                assertThrows(
                        InvalidRepairOrderItemException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order Item: Repair Order must have at least one workshop service to request approval.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsAlreadyAwaitingApproval() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.AWAITING_APPROVAL)
                .workshopServices(repairOrder.getWorkshopServices())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order must be in diagnosis to request approval.",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRepairOrderIsAlreadyApproved() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.APPROVED)
                .workshopServices(repairOrder.getWorkshopServices())
                .build();

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute("repair-order-1")
                );

        assertEquals(
                "Invalid Repair Order State: Repair Order must be in diagnosis to request approval.",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldNotPersistWhenRequestApprovalFails() {

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .workshopServices(new ArrayList<>())
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
    void shouldPersistTheSameRepairOrderAfterRequestingApproval() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute("repair-order-1");

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
                RepairOrderStatus.AWAITING_APPROVAL,
                captured.getStatus()
        );
    }
}