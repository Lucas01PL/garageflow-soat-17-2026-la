package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.CustomerSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.VehicleSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRepairOrderUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @Mock
    private ClientRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private CreateRepairOrderUseCase useCase;

    private Client sampleClient() {
        return new Client("cust1", "John Doe", "12345678900", "11999999999", "john@example.com", "Main St");
    }

    private Vehicle sampleVehicle() {
        return new Vehicle("vehicle1", "ABC1234", "Toyota", "Corolla", 2020);
    }

    @Test
    void shouldCreateRepairOrderSuccessfully() {
        RepairOrder input = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        Client client = sampleClient();
        Vehicle vehicle = sampleVehicle();

        RepairOrder saved = RepairOrder.builder()
                .id("ro1")
                .status(RepairOrderStatus.RECEIVED)
                .customer(CustomerSnapshot.from(client))
                .vehicle(VehicleSnapshot.from(vehicle))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(client));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(vehicle));
        when(repository.save(any())).thenReturn(saved);

        RepairOrder result = useCase.execute(input);

        assertNotNull(result);
        assertEquals("ro1", result.getId());

        ArgumentCaptor<RepairOrder> captor = ArgumentCaptor.forClass(RepairOrder.class);
        verify(repository).save(captor.capture());
        assertEquals(RepairOrderStatus.RECEIVED, captor.getValue().getStatus());
        assertEquals("cust1", captor.getValue().getCustomer().getCustomerId());
        assertEquals("vehicle1", captor.getValue().getVehicle().getVehicleId());
        assertNotNull(captor.getValue().getNumber());
    }

    @Test
    void shouldThrowWhenRepairOrderIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        assertEquals("Repair order cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCustomerIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot(""))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("Customer ID cannot be empty", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot("missing"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("Customer not found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenVehicleIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot(""))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleClient()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("Vehicle ID cannot be empty", ex.getMessage());
    }

    @Test
    void shouldThrowWhenVehicleNotFound() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("missing"))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleClient()));
        when(vehicleRepository.findById("missing")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(ro));
        assertEquals("Vehicle not found", ex.getMessage());
    }
}
