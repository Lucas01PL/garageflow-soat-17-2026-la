package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateRepairOrderUseCase {

    private RepairOrderRepository repository;
    private CustomerRepository customerRepository;
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;

    public RepairOrder execute(RepairOrder request) {
        if (request == null) {
            throw new RequiredObjectException("Repair Order");
        }

        Customer customer = getCustomer(request.getCustomer());

        CustomerSnapshot customerSnapshot = CustomerSnapshot.from(customer);

        Vehicle vehicle = getVehicle(request.getVehicle());

        VehicleSnapshot vehicleSnapshot = VehicleSnapshot.from(vehicle);

        User user = getUser(request.getUserId());

        RepairOrder repairOrder = RepairOrder.builder()
                                    .customer(customerSnapshot)
                                    .vehicle(vehicleSnapshot)
                                    .userId(user.getId())
                                    .build();
        repairOrder.number();
        repairOrder.received();

        return repository.save(repairOrder);
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

