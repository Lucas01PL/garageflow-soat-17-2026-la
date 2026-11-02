package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.WorkshopServiceService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.command.RemoveWorkshopServiceCommand;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InsufficientQuantityException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.WorkshopServiceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
class RemoveWorkshopServiceUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @Mock
    private WorkshopServiceRepository workshopServiceRepository;

    @Mock
    private RepairOrderFinder repairOrderFinder;

    private WorkshopServiceService workshopServiceService;

    private RemoveWorkshopServiceUseCase useCase;

    private RepairOrder repairOrder;
    private WorkshopService workshopService;
    private RemoveWorkshopServiceCommand command;

    @BeforeEach
    void setUp() {
        workshopService = new WorkshopService(
                "Troca de óleo",
                new BigDecimal("150.00")
        );

        workshopService.setId("workshop-service-1");

        WorkshopServiceSnapshot snapshot =
                WorkshopServiceSnapshot.from(
                        workshopService,
                        5
                );

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .workshopServices(
                        new ArrayList<>(
                                List.of(snapshot)
                        )
                )
                .build();

        command = new RemoveWorkshopServiceCommand(2);
        workshopServiceService = new WorkshopServiceService(workshopServiceRepository);
        useCase = new RemoveWorkshopServiceUseCase(
                repository,
                workshopServiceService,
                repairOrderFinder);
    }

    @Test
    void shouldRemoveWorkshopServiceSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        when(repository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute(
                        "repair-order-1",
                        "workshop-service-1",
                        command
                );

        assertNotNull(result);
        assertSame(repairOrder, result);

        assertEquals(
                1,
                result.getWorkshopServices().size()
        );

        WorkshopServiceSnapshot remainingService =
                result.getWorkshopServices().getFirst();

        assertEquals(
                "workshop-service-1",
                remainingService.getWorkshopServiceId()
        );

        assertEquals(
                3,
                remainingService.getQuantity()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(workshopServiceRepository)
                .findById("workshop-service-1");

        verify(repository)
                .save(repairOrder);
    }

    @Test
    void shouldRemoveEntireWorkshopServiceWhenRequestedQuantityEqualsCurrentQuantity() {

        command = new RemoveWorkshopServiceCommand(5);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        when(repository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute(
                        "repair-order-1",
                        "workshop-service-1",
                        command
                );

        assertNotNull(result);

        assertEquals(
                0,
                result.getWorkshopServices().size()
        );

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
                        () -> useCase.execute(
                                null,
                                "workshop-service-1",
                                command
                        )
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById(null);

        verifyNoInteractions(
                workshopServiceRepository,
                repository
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
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                command
                        )
                );

        assertNotNull(exception);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(
                workshopServiceRepository,
                repository
        );
    }

    @Test
    void shouldThrowWhenWorkshopServiceIdIsNull() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                null,
                                command
                        )
                );

        assertEquals(
                "Field 'workshopServiceId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(
                workshopServiceRepository,
                repository
        );
    }

    @Test
    void shouldThrowWhenWorkshopServiceIdIsBlank() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "  ",
                                command
                        )
                );

        assertEquals(
                "Field 'workshopServiceId' is required.",
                exception.getMessage()
        );

        verifyNoInteractions(
                workshopServiceRepository,
                repository
        );
    }

    @Test
    void shouldThrowWhenWorkshopServiceDoesNotExist() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                command
                        )
                );

        assertEquals(
                "Workshop service with id 'workshop-service-1' was not found.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(workshopServiceRepository)
                .findById("workshop-service-1");

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenWorkshopServiceDoesNotExistInRepairOrder() {

        repairOrder.removeWorkshopService(WorkshopServiceSnapshot.from(workshopService, 5));

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        WorkshopServiceNotFoundException exception =
                assertThrows(
                        WorkshopServiceNotFoundException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                command
                        )
                );

        assertEquals(
                "Workshop service with ID 'workshop-service-1' not found in repair order",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(workshopServiceRepository)
                .findById("workshop-service-1");

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenRequestedQuantityIsGreaterThanRepairOrderQuantity() {

        command = new RemoveWorkshopServiceCommand(6);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        InsufficientQuantityException exception =
                assertThrows(
                        InsufficientQuantityException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                command
                        )
                );

        assertEquals(
                "Quantity of Workshop Service 'workshop-service-1' is insufficient to remove the requested amount.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(workshopServiceRepository)
                .findById("workshop-service-1");

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenQuantityIsZero() {

        command = new RemoveWorkshopServiceCommand(0);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        InvalidFieldValueException exception =
                assertThrows(
                        InvalidFieldValueException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                command
                        )
                );

        assertEquals(
                "Field 'quantity' is invalid. Workshop service quantity must be greater than zero.",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {

        command = new RemoveWorkshopServiceCommand(-1);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        InvalidFieldValueException exception =
                assertThrows(
                        InvalidFieldValueException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "workshop-service-1",
                                command
                        )
                );

        assertEquals(
                "Field 'quantity' is invalid. Workshop service quantity must be greater than zero.",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldNotPersistWhenRemoveWorkshopServiceFails() {

        command = new RemoveWorkshopServiceCommand(10);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        assertThrows(
                InsufficientQuantityException.class,
                () -> useCase.execute(
                        "repair-order-1",
                        "workshop-service-1",
                        command
                )
        );

        verify(repository, never())
                .save(any(RepairOrder.class));
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterRemovingWorkshopService() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        when(repository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute(
                "repair-order-1",
                "workshop-service-1",
                command
        );

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
                3,
                captured.getWorkshopServices()
                        .getFirst()
                        .getQuantity()
        );
    }
}