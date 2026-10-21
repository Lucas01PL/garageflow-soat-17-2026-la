package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CreateRepairOrderUseCase {

    private RepairOrderRepository repository;
    private CustomerRepository customerRepository;
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    private PartRepository partRepository;
    private WorkshopServiceRepository workshopServiceRepository;

    public RepairOrder execute(RepairOrder request) {
        if (request == null) {
            throw new RequiredObjectException("Repair Order");
        }

        Customer customer = getCustomer(request.getCustomer());
        CustomerSnapshot customerSnapshot = CustomerSnapshot.from(customer);

        Vehicle vehicle = getVehicle(request.getVehicle());
        VehicleSnapshot vehicleSnapshot = VehicleSnapshot.from(vehicle);

        User user = getUser(request.getUserId());

        List<WorkshopServiceSnapshot> workshopServices = resolveWorkshopServices(request.getWorkshopServices());
        List<PartSnapshot> parts = resolveParts(request.getParts());

        RepairOrder repairOrder = RepairOrder.builder()
                .customer(customerSnapshot)
                .vehicle(vehicleSnapshot)
                .userId(user.getId())
                .workshopServices(workshopServices)
                .parts(parts)
                .build();
        repairOrder.number();
        repairOrder.received();

        return repository.save(repairOrder);
    }

    private List<WorkshopServiceSnapshot> resolveWorkshopServices(List<WorkshopServiceSnapshot> workshopServices) {
        if (workshopServices == null || workshopServices.isEmpty()) {
            return new ArrayList<>();
        }

        return workshopServices.stream()
                .map(this::resolveWorkshopServiceSnapshot)
                .toList();
    }

    private WorkshopServiceSnapshot resolveWorkshopServiceSnapshot(WorkshopServiceSnapshot snapshot) {
        if (snapshot == null) {
            throw new RequiredObjectException("Workshop service");
        }

        String workshopServiceId = snapshot.getWorkshopServiceId();
        if (workshopServiceId == null || workshopServiceId.isBlank()) {
            throw new RequiredFieldException("workshopServiceId");
        }

        WorkshopService workshopService = workshopServiceRepository.findById(workshopServiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop service", "id", workshopServiceId));

        Integer quantity = snapshot.getQuantity() == null ? 1 : snapshot.getQuantity();
        return WorkshopServiceSnapshot.from(workshopService, quantity);
    }

    private List<PartSnapshot> resolveParts(List<PartSnapshot> parts) {
        if (parts == null || parts.isEmpty()) {
            return new ArrayList<>();
        }

        return parts.stream()
                .map(this::resolvePartSnapshot)
                .toList();
    }

    private PartSnapshot resolvePartSnapshot(PartSnapshot snapshot) {
        if (snapshot == null) {
            throw new RequiredObjectException("Part");
        }

        String partId = snapshot.getId();
        if (partId == null || partId.isBlank()) {
            throw new RequiredFieldException("partId");
        }

        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part", "id", partId));

        Integer quantity = snapshot.getQuantity() == null ? 1 : snapshot.getQuantity();
        return PartSnapshot.from(part, quantity);
    }

    private Customer getCustomer(CustomerSnapshot customer) {
        if(customer == null) {
            throw new RequiredObjectException("Customer");
        }
        if(customer.getCustomerId() == null || customer.getCustomerId().isBlank()) {
            throw new RequiredFieldException("customerId");
        }

        return customerRepository.findById(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customer.getCustomerId()));
    }

    private Vehicle getVehicle(VehicleSnapshot vehicleSnapshot) {
        if(vehicleSnapshot == null) {
            throw new RequiredObjectException("Vehicle");
        }
        if(vehicleSnapshot.getVehicleId() == null || vehicleSnapshot.getVehicleId().isBlank()) {
            throw new RequiredFieldException("vehicleId");
        }
        return vehicleRepository.findById(vehicleSnapshot.getVehicleId()).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "vehicleId", vehicleSnapshot.getVehicleId()));
    }

    private User getUser(String userId) {
        if(userId == null || userId.isBlank()) {
            throw new RequiredFieldException("userId");
        }

        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
    }

}

