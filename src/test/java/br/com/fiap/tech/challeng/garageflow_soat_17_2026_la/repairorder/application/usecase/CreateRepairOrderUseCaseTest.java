package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.CustomerSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.VehicleSnapshot;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRepairOrderUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private CreateRepairOrderUseCase useCase;

    @Test
    void shouldCreateRepairOrderSuccessfully() {
        RepairOrder input = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .userId("user1")
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        RepairOrder saved = RepairOrder.builder()
                .status(RepairOrderStatus.FINISHED)
                .userId("user1")
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .id("ro1")
                .build();

        when(repository.save(any())).thenReturn(saved);

        RepairOrder result = useCase.execute(input);

        assertNotNull(result);
        assertEquals("ro1", result.getId());
        verify(repository).save(input);
    }

    @Test
    void shouldThrowWhenRepairOrderIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        assertEquals("Repair order cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCustomerIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("Customer ID cannot be empty", ex.getMessage());
    }

    @Test
    void shouldThrowWhenVehicleIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .customer(new CustomerSnapshot("cust1"))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("Vehicle ID cannot be empty", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUserIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("User ID cannot be empty", ex.getMessage());
    }
}

