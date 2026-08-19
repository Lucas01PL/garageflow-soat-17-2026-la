package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRepairOrderByIdUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private GetRepairOrderByIdUseCase useCase;

    @Test
    void shouldGetRepairOrderByIdSuccessfully() {
        RepairOrder ro = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .userId("user1")
                .id("ro1")
                .build();

        when(repository.findById("ro1")).thenReturn(Optional.of(ro));

        Optional<RepairOrder> result = useCase.execute("ro1");

        assertTrue(result.isPresent());
        assertEquals(ro.getId(), result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenRepairOrderNotFound() {
        when(repository.findById("notfound")).thenReturn(Optional.empty());

        Optional<RepairOrder> result = useCase.execute("notfound");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenIdIsEmpty() {
        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(""));
        assertEquals("Field 'id' is required.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(null));
        assertEquals("Field 'id' is required.", ex.getMessage());
    }
}

