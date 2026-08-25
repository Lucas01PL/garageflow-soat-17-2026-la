package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddRemoveWorkshopServiceRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
class AddWorkshopServiceUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @Mock
    private WorkshopServiceRepository workshopServiceRepository;

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @InjectMocks
    private AddWorkshopServiceUseCase useCase;

    private RepairOrder repairOrder;
    private WorkshopService workshopService;
    private AddRemoveWorkshopServiceRequest request;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .build();

        workshopService = new WorkshopService(
                "Troca de óleo",
                new BigDecimal("150.00")
        );

        workshopService.setId("workshop-service-1");

        request = new AddRemoveWorkshopServiceRequest();
        request.setWorkshopServiceId("workshop-service-1");
        request.setQuantity(2);
    }

    @Test
    void shouldAddWorkshopServiceSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        when(repository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute("repair-order-1", request);

        assertNotNull(result);
        assertSame(repairOrder, result);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(workshopServiceRepository)
                .findById("workshop-service-1");

        verify(repository)
                .save(repairOrder);

        assertEquals(1, repairOrder.getWorkshopServices().size());

        WorkshopServiceSnapshot snapshot =
                repairOrder.getWorkshopServices().getFirst();

        assertEquals(
                "workshop-service-1",
                snapshot.getWorkshopServiceId()
        );

        assertEquals(
                "Troca de óleo",
                snapshot.getDescription()
        );

        assertEquals(
                2,
                snapshot.getQuantity()
        );

        assertEquals(
                new BigDecimal("150.00"),
                snapshot.getUnitPrice()
        );
    }

    @Test
    void shouldThrowWhenRepairOrderIdIsInvalid() {

        when(repairOrderFinder.findById(" "))
                .thenThrow(
                        new RequiredFieldException("repairOrderId")
                );

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(" ", request)
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById(" ");

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
                                request
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

        request.setWorkshopServiceId(null);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
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

        request.setWorkshopServiceId(" ");

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
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
                                request
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
                .save(any());
    }

    @Test
    void shouldThrowWhenQuantityIsZero() {

        request.setQuantity(0);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
                        )
                );

        assertEquals(
                "Workshop service quantity must be greater than zero.",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {

        request.setQuantity(-1);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
                        )
                );

        assertEquals(
                "Workshop service quantity must be greater than zero.",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterAddingWorkshopService() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(workshopServiceRepository.findById("workshop-service-1"))
                .thenReturn(Optional.of(workshopService));

        when(repository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute("repair-order-1", request);

        ArgumentCaptor<RepairOrder> captor =
                ArgumentCaptor.forClass(RepairOrder.class);

        verify(repository)
                .save(captor.capture());

        assertSame(
                repairOrder,
                captor.getValue()
        );

        assertEquals(
                1,
                captor.getValue()
                        .getWorkshopServices()
                        .size()
        );
    }
}