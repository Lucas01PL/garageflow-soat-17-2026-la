package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllRepairOrdersUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private ListAllRepairOrdersUseCase useCase;

    @Test
    void shouldListAllRepairOrdersSuccessfully() {
        RepairOrder ro1 = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .total(new BigDecimal("500.00"))
                .userId("user1")
                .id("ro1")
                .build();

        RepairOrder ro2 = RepairOrder.builder()
                .status(RepairOrderStatus.FINISHED)
                .total(new BigDecimal("100.00"))
                .userId("user2")
                .id("ro2")
                .build();

        when(repository.findAll()).thenReturn(List.of(ro1, ro2));

        List<RepairOrder> result = useCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ro1, result.get(0));
        assertEquals(ro2, result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenNoRepairOrders() {
        when(repository.findAll()).thenReturn(List.of());

        List<RepairOrder> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

