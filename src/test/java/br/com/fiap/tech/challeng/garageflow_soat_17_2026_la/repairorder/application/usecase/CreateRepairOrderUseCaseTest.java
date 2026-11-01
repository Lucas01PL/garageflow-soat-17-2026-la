package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.PartService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.WorkshopServiceService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderPartStockCoordinator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.NotEnoughResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
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

    @Mock
    private PartRepository partRepository;

    @Mock
    private WorkshopServiceRepository workshopServiceRepository;

    @Mock
    private PartStockControlUseCase partStockControlUseCase;

    private WorkshopServiceService workshopServiceService;

    private PartService partService;

    private RepairOrderPartStockCoordinator partStockCoordinator;

    @BeforeEach
    void setUp() {
        workshopServiceService = new WorkshopServiceService(workshopServiceRepository);
        partService = new PartService(partRepository);
        partStockCoordinator = new RepairOrderPartStockCoordinator(partStockControlUseCase);
        useCase = new CreateRepairOrderUseCase(
                repository,
                customerRepository,
                vehicleRepository,
                userRepository,
                workshopServiceService,
                partService,
                partStockCoordinator);
    }

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
    void shouldCreateRepairOrderWithWorkshopServicesAndPartsFromIds() {
        WorkshopService workshopService = new WorkshopService("Alignment", new BigDecimal("150.00"));
        workshopService.setId("ws-1");

        Part part = new Part("part-1", "P-001", "Brake Pad", 5, new BigDecimal("80.00"));

        RepairOrder input = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .userId("user1")
                .workshopServices(List.of(new WorkshopServiceSnapshot("ws-1", 2)))
                .parts(List.of(new PartSnapshot("part-1", 3)))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleCustomer()));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(sampleVehicle()));
        when(userRepository.findById("user1")).thenReturn(Optional.of(new User("user1", "John Doe", "john@example.com", "ATIVO", UserRole.ADMIN)));
        when(workshopServiceRepository.findById("ws-1")).thenReturn(Optional.of(workshopService));
        when(partRepository.findById("part-1")).thenReturn(Optional.of(part));
        when(partStockControlUseCase.debitPartStock("part-1", 3)).thenReturn("Debited successfully");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RepairOrder result = useCase.execute(input);

        assertEquals(1, result.getWorkshopServices().size());
        assertEquals("ws-1", result.getWorkshopServices().getFirst().getWorkshopServiceId());
        assertEquals(2, result.getWorkshopServices().getFirst().getQuantity());
        assertEquals(new BigDecimal("150.00"), result.getWorkshopServices().getFirst().getUnitPrice());
        assertEquals(1, result.getParts().size());
        assertEquals("part-1", result.getParts().getFirst().getId());
        assertEquals(3, result.getParts().getFirst().getQuantity());
        assertEquals(new BigDecimal("80.00"), result.getParts().getFirst().getUnitPrice());
        assertEquals(new BigDecimal("300.00"), result.getTotalServices());
        assertEquals(new BigDecimal("240.00"), result.getTotalParts());
        assertEquals(new BigDecimal("540.00"), result.getTotal());
        verify(partStockControlUseCase).debitPartStock("part-1", 3);
    }

    @Test
    void shouldKeepUseCaseQuantityValidationBeforeDebitingStock() {
        Part part = new Part("part-1", "P-001", "Brake Pad", 5, BigDecimal.valueOf(80.00));
        RepairOrder input = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .userId("user1")
                .parts(List.of(new PartSnapshot("part-1", 0)))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleCustomer()));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(sampleVehicle()));
        when(userRepository.findById("user1")).thenReturn(Optional.of(new User("user1", "John Doe", "john@example.com", "ATIVO", UserRole.ADMIN)));
        when(partRepository.findById("part-1")).thenReturn(Optional.of(part));

        assertThrows(InvalidFieldValueException.class, () -> useCase.execute(input));

        verifyNoInteractions(partStockControlUseCase);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRollbackPreviouslyDebitedPartsWhenAnotherPartHasInsufficientStock() {
        Part firstPart = new Part("part-1", "P-001", "Brake Pad", 5, BigDecimal.valueOf(80.00));
        Part secondPart = new Part("part-2", "P-002", "Oil Filter", 0, BigDecimal.valueOf(20.00));
        RepairOrder input = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .userId("user1")
                .parts(List.of(new PartSnapshot("part-1", 2), new PartSnapshot("part-2", 1)))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleCustomer()));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(sampleVehicle()));
        when(userRepository.findById("user1")).thenReturn(Optional.of(new User("user1", "John Doe", "john@example.com", "ATIVO", UserRole.ADMIN)));
        when(partRepository.findById("part-1")).thenReturn(Optional.of(firstPart));
        when(partRepository.findById("part-2")).thenReturn(Optional.of(secondPart));
        when(partStockControlUseCase.debitPartStock("part-1", 2)).thenReturn("Debited successfully");
        when(partStockControlUseCase.debitPartStock("part-2", 1))
                .thenThrow(new NotEnoughResourceException("Not enough stock"));

        NotEnoughResourceException exception = assertThrows(
                NotEnoughResourceException.class,
                () -> useCase.execute(input));

        assertEquals("Not enough stock", exception.getMessage());
        verify(partStockControlUseCase).addPartStock("part-1", 2);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldRollbackDebitedPartsWhenRepairOrderCannotBePersisted() {
        Part part = new Part("part-1", "P-001", "Brake Pad", 5, BigDecimal.valueOf(80.00));
        RepairOrder input = RepairOrder.builder()
                .customer(new CustomerSnapshot("cust1"))
                .vehicle(new VehicleSnapshot("vehicle1"))
                .userId("user1")
                .parts(List.of(new PartSnapshot("part-1", 2)))
                .build();

        when(customerRepository.findById("cust1")).thenReturn(Optional.of(sampleCustomer()));
        when(vehicleRepository.findById("vehicle1")).thenReturn(Optional.of(sampleVehicle()));
        when(userRepository.findById("user1")).thenReturn(Optional.of(new User("user1", "John Doe", "john@example.com", "ATIVO", UserRole.ADMIN)));
        when(partRepository.findById("part-1")).thenReturn(Optional.of(part));
        when(partStockControlUseCase.debitPartStock("part-1", 2)).thenReturn("Debited successfully");
        when(repository.save(any())).thenThrow(new IllegalStateException("Database unavailable"));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(input));

        assertEquals("Database unavailable", exception.getMessage());
        verify(partStockControlUseCase).addPartStock("part-1", 2);
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
