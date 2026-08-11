package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class CreateRepairOrderUseCase {

    private RepairOrderRepository repository;
    private ClientRepository customerRepository;
    private VehicleRepository vehicleRepository;

    public RepairOrder execute(RepairOrder request) {
        if (request == null) {
            throw new IllegalArgumentException("Repair order cannot be null");
        }

        Client customer = getCustomer(request.getCustomer().getCustomerId());

        CustomerSnapshot customerSnapshot = CustomerSnapshot.from(customer);

        Vehicle vehicle = getVehicle(request.getVehicle().getVehicleId());

        VehicleSnapshot vehicleSnapshot = VehicleSnapshot.from(vehicle);

        RepairOrder repairOrder = RepairOrder.builder()
                                    .customer(customerSnapshot)
                                    .vehicle(vehicleSnapshot)
                                    .build();
        repairOrder.number();
        repairOrder.received();

        return repository.save(repairOrder);
    }

    private Client getCustomer(String customerId) {
        if(customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }

        return customerRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    private Vehicle getVehicle(String vehicleId) {
        if(vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
    }

}

