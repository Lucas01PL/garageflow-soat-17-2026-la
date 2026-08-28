package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListRepairOrdersByCustomerUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private ListRepairOrdersByCustomerUseCase useCase;

    @Test
    void shouldReturnRepairOrdersByCustomer() {

        RepairOrder first =
                RepairOrder.builder()
                        .id("repair-order-1")
                        .build();

        RepairOrder second =
                RepairOrder.builder()
                        .id("repair-order-2")
                        .build();

        when(repository.findByCustomerId("customer-1"))
                .thenReturn(List.of(first, second));

        List<RepairOrder> result =
                useCase.execute("customer-1");

        assertEquals(
                2,
                result.size()
        );

        assertSame(
                first,
                result.get(0)
        );

        assertSame(
                second,
                result.get(1)
        );

        verify(repository)
                .findByCustomerId("customer-1");
    }

    @Test
    void shouldReturnEmptyListWhenCustomerHasNoRepairOrders() {

        when(repository.findByCustomerId("customer-1"))
                .thenReturn(List.of());

        List<RepairOrder> result =
                useCase.execute("customer-1");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findByCustomerId("customer-1");
    }

    @Test
    void shouldThrowWhenCustomerIdIsNull() {

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(null)
                );

        assertEquals(
                "Field 'customerId' is required.",
                exception.getMessage()
        );

        verifyNoInteractions(repository);
    }

    @Test
    void shouldThrowWhenCustomerIdIsBlank() {

        RequiredFieldException exception =
                assertThrows(
                        RequiredFieldException.class,
                        () -> useCase.execute(" ")
                );

        assertEquals(
                "Field 'customerId' is required.",
                exception.getMessage()
        );

        verifyNoInteractions(repository);
    }
}