package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.CustomerSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.VehicleSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRepairOrderUseCaseTest {

    @Mock
    private RepairOrderRepository repository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateRepairOrderUseCase useCase;

    private Customer sampleCustomer() {
        return new Customer("cust1", "John Doe", "12345678900", "11999999999", "john@example.com", "Main St");
    }

    private Vehicle sampleVehicle() {
        return new Vehicle("vehicle1", "ABC1234", "Toyota", "Corolla", 2020);
    }

    @Test
    void shouldCreateRepairOrderSuccessfully() {
        RepairOrder input = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .userId("user1")
                .build();

        Customer customer = sampleCustomer();
        Vehicle vehicle = sampleVehicle();

        RepairOrder saved = RepairOrder.builder()
                .id("ro1")
                .status(RepairOrderStatus.RECEIVED)
                .customer(CustomerSnapshot.from(customer))
                .vehicle(VehicleSnapshot.from(vehicle))
                .userId("user1")
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(customer));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(vehicle));
        when(userRepository.findById("user1")).thenReturn(Optional.of(new User("user1", "John Doe", "john@example.com", "ATIVO", UserRole.ADMIN)));
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
        RequiredObjectException ex = assertThrows(RequiredObjectException.class, () -> useCase.execute(null));
        assertEquals("Repair Order cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCustomerIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot(""))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(ro));
        assertEquals("Field 'customerId' is required.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot("missing"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .build();

        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> useCase.execute(ro));
        assertEquals("Customer with customerId 'missing' was not found.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenVehicleIdIsEmpty() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot(""))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleCustomer()));

        RequiredFieldException ex = assertThrows(RequiredFieldException.class, () -> useCase.execute(ro));
        assertEquals("Field 'vehicleId' is required.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenVehicleNotFound() {
        RepairOrder ro = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("missing"))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleCustomer()));
        when(vehicleRepository.findById("missing")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> useCase.execute(ro));
        assertEquals("Vehicle with vehicleId 'missing' was not found.", ex.getMessage());
    }
}
