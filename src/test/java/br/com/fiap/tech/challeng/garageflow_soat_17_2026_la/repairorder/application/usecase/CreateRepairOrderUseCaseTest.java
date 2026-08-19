package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.CustomerSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.VehicleSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRepairOrderUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @InjectMocks
    private CreateRepairOrderUseCase useCase;

    @Mock
    private ClientRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

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

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(new Client("cust1", "John Doe", "john.doe@example.com", "", "")));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(new Vehicle("vehicle1", "Toyota Camry", "ABC123", "", null)));
        when(repository.save(any())).thenReturn(saved);

        RepairOrder result = useCase.execute(input);

        assertNotNull(result);
        assertEquals("ro1", result.getId());
    }

    @Test
    void shouldThrowWhenRepairOrderIsNull() {
        RequiredObjectException ex = assertThrows(RequiredObjectException.class, () -> useCase.execute(null));
        assertEquals("Repair Order cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCustomerIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();
        RequiredObjectException ex = assertThrows(RequiredObjectException.class, () -> useCase.execute(ro));
        assertEquals("Customer cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenVehicleIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot(""))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(new Client("cust1", "John Doe", "john.doe@example.com", "", "")));

        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(ro));
        assertEquals("Field 'vehicleId' is required.", ex.getMessage());
    }
}

