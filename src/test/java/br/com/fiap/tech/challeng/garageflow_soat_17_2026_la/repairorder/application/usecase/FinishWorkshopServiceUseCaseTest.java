package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderItemException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.WorkshopServiceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.FinishWorkshopServiceRequest;
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
class FinishWorkshopServiceUseCaseTest {

    @Mock
    private RepairOrderRepository repairOrderRepository;

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @InjectMocks
    private FinishWorkshopServiceUseCase useCase;

    private RepairOrder repairOrder;
    private WorkshopServiceSnapshot workshopService;
    private FinishWorkshopServiceRequest request;

    @BeforeEach
    void setUp() {
        workshopService = createWorkshopService();

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_EXECUTION)
                .workshopServices(
                        new ArrayList<>(
                                List.of(workshopService)
                        )
                )
                .build();

        request = new FinishWorkshopServiceRequest();
        request.setDurationInMinutes(45);
    }

    @Test
    void shouldFinishWorkshopServiceSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute(
                        "repair-order-1",
                        "workshop-service-1",
                        request
                );

        assertNotNull(result);
        assertSame(repairOrder, result);

        WorkshopServiceSnapshot resultService =
                result.getWorkshopServices().getFirst();

        assertEquals(
                WorkshopServiceStatus.FINISHED,
                resultService.getStatus()
        );

        assertEquals(
                45,
                resultService.getDurationInMinutes()
        );

        assertNotNull(resultService.getFinishedAt());

        /*
         * Como este é o último serviço da OS,
         * o Aggregate deve colocar a OS em FINISHED.
         */
        assertEquals(
                RepairOrderStatus.FINISHED,
                result.getStatus()
        );

        assertNotNull(result.getFinishDate());

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
                                "workshop-service-1",
                                request
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
                                "workshop-service-1",
                                request
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
                                "workshop-service-not-found",
                                request
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
    void shouldThrowWhenWorkshopServiceIsNotInExecution() {

        workshopService.setStatus(
                WorkshopServiceStatus.WAITING_ATTENDING
        );

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderStateException exception =
                assertThrows(
                        InvalidRepairOrderStateException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                request
                        )
                );

        assertEquals(
                "Invalid Repair Order State: Workshop service not in execution",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenDurationIsZero() {

        request.setDurationInMinutes(0);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderItemException exception =
                assertThrows(
                        InvalidRepairOrderItemException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                request
                        )
                );

        assertEquals(
                "Invalid Repair Order Item: Duration must be greater than zero.",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenDurationIsNegative() {

        request.setDurationInMinutes(-10);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidRepairOrderItemException exception =
                assertThrows(
                        InvalidRepairOrderItemException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                request
                        )
                );

        assertEquals(
                "Invalid Repair Order Item: Duration must be greater than zero.",
                exception.getMessage()
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldNotPersistWhenFinishingWorkshopServiceFails() {

        workshopService.setStatus(
                WorkshopServiceStatus.FINISHED
        );

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        assertThrows(
                InvalidRepairOrderStateException.class,
                () -> useCase.execute(
                        "repair-order-1",
                        "workshop-service-1",
                        request
                )
        );

        verify(repairOrderRepository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterFinishingWorkshopService() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(repairOrderRepository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute(
                "repair-order-1",
                "workshop-service-1",
                request
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
                WorkshopServiceStatus.FINISHED,
                captured.getWorkshopServices()
                        .getFirst()
                        .getStatus()
        );
    }

    private WorkshopServiceSnapshot createWorkshopService() {

        WorkshopServiceSnapshot snapshot =
                new WorkshopServiceSnapshot();

        snapshot.setWorkshopServiceId("workshop-service-1");
        snapshot.setDescription("Troca de óleo");
        snapshot.setQuantity(1);
        snapshot.setUnitPrice(
                new BigDecimal("150.00")
        );
        snapshot.setStatus(WorkshopServiceStatus.IN_EXECUTION);

        return snapshot;
    }
}