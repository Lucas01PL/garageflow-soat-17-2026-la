package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.WorkshopServiceNotFoundException;
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
class StartWorkshopServiceUseCaseTest {

    @Mock
    private RepairOrderRepository repairOrderRepository;

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @InjectMocks
    private StartWorkshopServiceUseCase useCase;

    private RepairOrder repairOrder;
    private WorkshopServiceSnapshot workshopService;

    @BeforeEach
    void setUp() {

        workshopService = new WorkshopServiceSnapshot();

        workshopService.setWorkshopServiceId("workshop-service-1");
        workshopService.setDescription("Troca de óleo");
        workshopService.setQuantity(1);
        workshopService.setUnitPrice(
                new BigDecimal("150.00")
        );
        workshopService.setStatus(
                WorkshopServiceStatus.WAITING_ATTENDING
        );

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_EXECUTION)
                .workshopServices(
                        new ArrayList<>(
                                List.of(workshopService)
                        )
                )
                .build();
    }

    @Test
    void shouldStartWorkshopServiceSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute(
                        "repair-order-1",
                        "workshop-service-1"
                );

        assertNotNull(result);
        assertSame(repairOrder, result);

        WorkshopServiceSnapshot resultService =
                result.getWorkshopServices().getFirst();

        assertEquals(
                WorkshopServiceStatus.IN_EXECUTION,
                resultService.getStatus()
        );

        assertNotNull(resultService.getStartedAt());

        assertEquals(
                RepairOrderStatus.IN_EXECUTION,
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
                        () -> useCase.execute(
                                null,
                                "workshop-service-1"
                        )
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
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1"
                        )
                );

        assertNotNull(exception);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(repairOrderRepository);
    }

    @Test
    void shouldThrowWhenWorkshopServiceDoesNotExistInRepairOrder() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        WorkshopServiceNotFoundException exception =
                assertThrows(
                        WorkshopServiceNotFoundException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-not-found"
                        )
                );

        assertEquals(
                "Workshop service with ID 'workshop-service-not-found' not found in repair order",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenWorkshopServiceIsNotWaitingAttending() {

        workshopService.setStatus(
                WorkshopServiceStatus.IN_EXECUTION
        );

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1"
                        )
                );

        assertEquals(
                "Invalid Repair Order State: Workshop service not waiting attending",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenWorkshopServiceIsAlreadyFinished() {

        workshopService.setStatus(
                WorkshopServiceStatus.FINISHED
        );

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1"
                        )
                );

        assertEquals(
                "Invalid Repair Order State: Workshop service not waiting attending",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenWorkshopServiceIdIsNull() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        WorkshopServiceNotFoundException exception =
                assertThrows(
                        WorkshopServiceNotFoundException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                null
                        )
                );

        assertEquals(
                "Workshop service with ID 'null' not found in repair order",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldNotPersistWhenStartWorkshopServiceFails() {

        workshopService.setStatus(
                WorkshopServiceStatus.FINISHED
        );

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute(
                        "repair-order-1",
                        "workshop-service-1"
                )
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterStartingWorkshopService() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute(
                "repair-order-1",
                "workshop-service-1"
        );

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
                WorkshopServiceStatus.IN_EXECUTION,
                captured.getWorkshopServices()
                        .getFirst()
                        .getStatus()
        );
    }
}