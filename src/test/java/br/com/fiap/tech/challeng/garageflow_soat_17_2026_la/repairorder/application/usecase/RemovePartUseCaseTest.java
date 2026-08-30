package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InsufficientQuantityException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidPartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddPartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.RemovePartRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemovePartUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @Mock
    private PartRepository partRepository;

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @Mock
    private PartStockControlUseCase partStockControlUseCase;

    @InjectMocks
    private RemovePartUseCase useCase;

    private RepairOrder repairOrder;
    private Part part;
    private RemovePartRequest request;

    @BeforeEach
    void setUp() {
        part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        PartSnapshot partSnapshot =
                PartSnapshot.from(part, 5);

        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.RECEIVED)
                .parts(new ArrayList<>(
                        List.of(partSnapshot)
                ))
                .build();

        request = new RemovePartRequest();
        request.setQuantity(2);
    }

    @Test
    void shouldRemovePartSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.addPartStock(
                "part-1",
                2
        )).thenReturn("Stock updated");

        when(repository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute(
                        "repair-order-1",
                        "part-1",
                        request
                );

        assertNotNull(result);
        assertSame(repairOrder, result);

        assertEquals(
                1,
                result.getParts().size()
        );

        assertEquals(
                3,
                result.getParts()
                        .getFirst()
                        .getQuantity()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(partRepository)
                .findById("part-1");

        verify(partStockControlUseCase)
                .addPartStock("part-1", 2);

        verify(repository)
                .save(repairOrder);
    }

    @Test
    void shouldRemoveEntirePartWhenRequestedQuantityEqualsCurrentQuantity() {

        request.setQuantity(5);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.addPartStock(
                "part-1",
                5
        )).thenReturn("Stock updated");

        when(repository.save(repairOrder))
                .thenReturn(repairOrder);

        RepairOrder result =
                useCase.execute(
                        "repair-order-1",
                        "part-1",
                        request
                );

        assertNotNull(result);

        assertEquals(
                0,
                result.getParts().size()
        );

        verify(partStockControlUseCase)
                .addPartStock("part-1", 5);

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
                        () -> useCase.execute(null, "part-1", request)
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById(null);

        verifyNoInteractions(
                partRepository,
                partStockControlUseCase,
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
                                "part-1",
                                request
                        )
                );

        assertNotNull(exception);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(
                partRepository,
                partStockControlUseCase,
                repository
        );
    }

    @Test
    void shouldThrowWhenPartIdIsNull() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidPartException exception =
                assertThrows(
                        InvalidPartException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                null,
                                request
                        )
                );

        assertEquals(
                "Invalid Part: Part ID cannot be empty",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(
                partRepository,
                partStockControlUseCase,
                repository
        );
    }

    @Test
    void shouldThrowWhenPartIdIsBlank() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidPartException exception =
                assertThrows(
                        InvalidPartException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "   ",
                                request
                        )
                );

        assertEquals(
                "Invalid Part: Part ID cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(
                partRepository,
                partStockControlUseCase,
                repository
        );
    }

    @Test
    void shouldThrowWhenPartDoesNotExist() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "part-1",
                                request
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("part-1")
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(partRepository)
                .findById("part-1");

        verifyNoInteractions(
                partStockControlUseCase,
                repository
        );
    }

    @Test
    void shouldThrowWhenPartDoesNotExistInRepairOrder() {

        repairOrder.removePart(PartSnapshot.from(part, 5));

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        PartNotFoundException exception =
                assertThrows(
                        PartNotFoundException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "part-1",
                                request
                        )
                );

        assertEquals(
                "Part with ID 'part-1' not found in repair order",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(partRepository)
                .findById("part-1");

    }

    @Test
    void shouldThrowWhenRequestedQuantityIsGreaterThanRepairOrderQuantity() {

        request.setQuantity(6);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        InsufficientQuantityException exception =
                assertThrows(
                        InsufficientQuantityException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "part-1",
                                request
                        )
                );

        assertEquals(
                "Quantity of Part 'part-1' is insufficient to remove the requested amount.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(partRepository)
                .findById("part-1");
    }

    @Test
    void shouldThrowWhenStockUpdateFails() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.addPartStock(
                "part-1",
                2
        )).thenThrow(
                new RuntimeException("Database unavailable")
        );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                "part-1",
                                request
                        )
                );

        assertEquals(
                "Failed to add part stock: Database unavailable",
                exception.getMessage()
        );

        verify(partStockControlUseCase)
                .addPartStock("part-1", 2);

        verify(repository, never())
                .save(any(RepairOrder.class));

        assertEquals(
                1,
                repairOrder.getParts().size()
        );

        assertEquals(
                5,
                repairOrder.getParts()
                        .getFirst()
                        .getQuantity()
        );
    }

    @Test
    void shouldPersistTheSameRepairOrderAfterRemovingPart() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.addPartStock(
                "part-1",
                2
        )).thenReturn("Stock updated");

        when(repository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        useCase.execute(
                "repair-order-1",
                "part-1",
                request
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
                captured.getParts()
                        .getFirst()
                        .getQuantity()
        );
    }
}