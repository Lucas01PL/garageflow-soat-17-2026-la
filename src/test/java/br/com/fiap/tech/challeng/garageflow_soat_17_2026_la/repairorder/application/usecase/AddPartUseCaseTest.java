package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderFinder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidPartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartStockOperationException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddRemovePartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.NotEnoughResourceException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddPartUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @Mock
    private PartRepository partRepository;

    @Mock
    private RepairOrderFinder repairOrderFinder;

    @Mock
    private PartStockControlUseCase partStockControlUseCase;

    @InjectMocks
    private AddPartUseCase useCase;

    private RepairOrder repairOrder;
    private Part part;
    private AddRemovePartRequest request;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .status(RepairOrderStatus.IN_DIAGNOSIS)
                .build();

        part = new Part(
                "part-1",
                "FIL-001",
                "Filtro de óleo",
                10,
                new BigDecimal("50.00")
        );

        request = new AddRemovePartRequest();
        request.setPartId("part-1");
        request.setQuantity(2);
    }

    @Test
    void shouldAddPartSuccessfully() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(repository.save(any(RepairOrder.class)))
                .thenReturn(repairOrder);

        when(partStockControlUseCase.debitPartStock("part-1", 2))
                .thenReturn("Debited successfully");

        RepairOrder result =
                useCase.execute("repair-order-1", request);

        assertNotNull(result);
        assertSame(repairOrder, result);

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verify(partRepository)
                .findById("part-1");

        verify(partStockControlUseCase)
                .debitPartStock("part-1", 2);

        verify(repository)
                .save(repairOrder);

        assertEquals(1, repairOrder.getParts().size());

        PartSnapshot snapshot =
                repairOrder.getParts().getFirst();

        assertEquals("part-1", snapshot.getId());
        assertEquals("Filtro de óleo", snapshot.getDescription());
        assertEquals(2, snapshot.getQuantity());
        assertEquals(
                new BigDecimal("50.00"),
                snapshot.getUnitPrice()
        );
    }

    @Test
    void shouldThrowWhenRepairOrderIdIsNull() {

        AddRemovePartRequest partRequest = new AddRemovePartRequest();
        partRequest.setPartId("part-1");
        partRequest.setQuantity(1);

        when(repairOrderFinder.findById(null))
                .thenThrow(new RequiredFieldException("repairOrderId"));

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(null, partRequest)
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verify(repairOrderFinder)
                .findById(null);

        verifyNoInteractions(
                partRepository,
                repository,
                partStockControlUseCase
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
                        () -> useCase.execute("repair-order-1", request)
                );

        assertTrue(
                exception.getMessage()
                        .contains("repair-order-1")
        );

        verify(repairOrderFinder)
                .findById("repair-order-1");

        verifyNoInteractions(
                partRepository,
                repository,
                partStockControlUseCase
        );
    }

    @Test
    void shouldThrowWhenPartIdIsNull() {

        request.setPartId(null);

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidPartException exception =
                assertThrows(
                        InvalidPartException.class,
                        () -> useCase.execute(
                                "repair-order-1",
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
                repository,
                partStockControlUseCase
        );
    }

    @Test
    void shouldThrowWhenPartIdIsBlank() {

        request.setPartId(" ");

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        InvalidPartException exception =
                assertThrows(
                        InvalidPartException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
                        )
                );

        assertEquals(
                "Invalid Part: Part ID cannot be empty",
                exception.getMessage()
        );

        verifyNoInteractions(
                partRepository,
                repository,
                partStockControlUseCase
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
    void shouldThrowWhenStockIsInsufficient() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.debitPartStock(
                "part-1",
                2
        )).thenThrow(
                new NotEnoughResourceException(
                        "Not enough stock"
                )
        );

        NotEnoughResourceException exception =
                assertThrows(
                        NotEnoughResourceException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
                        )
                );

        assertEquals(
                "Not enough stock",
                exception.getMessage()
        );

        verify(partStockControlUseCase)
                .debitPartStock("part-1", 2);

        verify(repository, never())
                .save(any());

        /*
         * IMPORTANTE:
         * O AddPartUseCase adiciona o snapshot antes de
         * debitar o estoque. Portanto, com a implementação
         * atual, a OS já foi alterada em memória quando a
         * exceção ocorre.
         */
        assertEquals(1, repairOrder.getParts().size());
    }

    @Test
    void shouldThrowWhenStockControlFailsUnexpectedly() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.debitPartStock(
                "part-1",
                2
        )).thenThrow(
                new RuntimeException("Database unavailable")
        );

        PartStockOperationException exception =
                assertThrows(
                        PartStockOperationException.class,
                        () -> useCase.execute(
                                "repair-order-1",
                                request
                        )
                );

        assertEquals(
                "Failed to debit part stock: Database unavailable",
                exception.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldPersistRepairOrderAfterSuccessfullyDebitingStock() {

        when(repairOrderFinder.findById("repair-order-1"))
                .thenReturn(repairOrder);

        when(partRepository.findById("part-1"))
                .thenReturn(Optional.of(part));

        when(partStockControlUseCase.debitPartStock(
                "part-1",
                2
        )).thenReturn("Debited successfully");

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

        verify(partStockControlUseCase)
                .debitPartStock("part-1", 2);
    }
}