package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateRepairOrderUseCase {

    private RepairOrderRepository repository;
    private ClientRepository customerRepository;
    private VehicleRepository vehicleRepository;

    public RepairOrder execute(RepairOrder request) {
        if (request == null) {
            throw new RequiredObjectException("Repair Order");
        }

        Client customer = getCustomer(request.getCustomer());

        CustomerSnapshot customerSnapshot = CustomerSnapshot.from(customer);

        Vehicle vehicle = getVehicle(request.getVehicle());

        VehicleSnapshot vehicleSnapshot = VehicleSnapshot.from(vehicle);

        RepairOrder repairOrder = RepairOrder.builder()
                                    .customer(customerSnapshot)
                                    .vehicle(vehicleSnapshot)
                                    .build();
        repairOrder.number();
        repairOrder.received();

        return repository.save(repairOrder);
    }

    private Client getCustomer(CustomerSnapshot customer) {
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

}

