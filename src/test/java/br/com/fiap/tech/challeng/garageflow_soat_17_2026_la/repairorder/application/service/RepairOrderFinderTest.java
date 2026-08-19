package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairOrderFinderTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private RepairOrderFinder finder;

    @Test
    void shouldFindRepairOrderSuccessfully() {

        RepairOrder repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .build();

        when(repository.findById("repair-order-1"))
                .thenReturn(Optional.of(repairOrder));

        RepairOrder result =
                finder.findById("repair-order-1");

        assertSame(
                repairOrder,
                result
        );

        verify(repository)
                .findById("repair-order-1");
    }

    @Test
    void shouldThrowWhenRepairOrderIdIsNull() {

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> finder.findById(null)
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verifyNoInteractions(repository);
    }

    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void shouldThrowWhenRepairOrderIdIsInvalid() {

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> finder.findById("")
                );

        assertEquals(
                "Field 'repairOrderId' is required.",
                exception.getMessage()
        );

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenRepairOrderDoesNotExist() {

        when(repository.findById("repair-order-1"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> finder.findById("repair-order-1")
                );

        assertEquals(
                "Repair Order with id 'repair-order-1' was not found.",
                exception.getMessage()
        );

        verify(repository)
                .findById("repair-order-1");
    }

    @Test
    void shouldReturnDifferentRepairOrdersForDifferentIds() {

        RepairOrder firstRepairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .build();

        RepairOrder secondRepairOrder = RepairOrder.builder()
                .id("repair-order-2")
                .build();

        when(repository.findById("repair-order-1"))
                .thenReturn(Optional.of(firstRepairOrder));

        when(repository.findById("repair-order-2"))
                .thenReturn(Optional.of(secondRepairOrder));

        RepairOrder firstResult =
                finder.findById("repair-order-1");

        RepairOrder secondResult =
                finder.findById("repair-order-2");

        assertSame(
                firstRepairOrder,
                firstResult
        );

        assertSame(
                secondRepairOrder,
                secondResult
        );

        verify(repository)
                .findById("repair-order-1");

        verify(repository)
                .findById("repair-order-2");
    }
}