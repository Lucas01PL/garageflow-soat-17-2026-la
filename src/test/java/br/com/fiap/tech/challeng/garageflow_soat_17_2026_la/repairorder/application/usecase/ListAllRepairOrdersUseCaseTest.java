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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllRepairOrdersUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private ListAllRepairOrdersUseCase useCase;

    @Test
    void shouldListAllRepairOrdersSuccessfully() {
        RepairOrder ro2 = RepairOrder.builder()
                .status(RepairOrderStatus.FINISHED)
                .total(new BigDecimal("100.00"))
                .userId("user2")
                .id("ro2")
                .build();

        when(repository.listAll(anyList())).thenReturn(List.of(ro2));

        List<RepairOrder> result = useCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ro2, result.getFirst());
    }

    @Test
    void shouldReturnEmptyListWhenNoRepairOrders() {
        when(repository.listAll(anyList())).thenReturn(List.of());

        List<RepairOrder> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

