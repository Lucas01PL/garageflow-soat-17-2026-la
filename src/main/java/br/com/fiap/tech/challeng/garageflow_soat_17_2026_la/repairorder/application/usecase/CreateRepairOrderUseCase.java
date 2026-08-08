package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class CreateRepairOrderUseCase {

    private RepairOrderRepository repository;
//    private CustomerRepository customerRepository;
//    private VehicleRepository vehicleRepository;

    public RepairOrder execute(RepairOrder repairOrder) {
        if (repairOrder == null) {
            throw new IllegalArgumentException("Repair order cannot be null");
        }
        if (repairOrder.getCustomer() == null || repairOrder.getCustomer().getId().isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (repairOrder.getVehicle() == null || repairOrder.getVehicle().getVehicleId().isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty");
        }
        if (repairOrder.getUserId() == null || repairOrder.getUserId().isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        validateCustomer(repairOrder.getCustomer());
        validateVehicle(repairOrder.getVehicle());

        repairOrder.number();
        repairOrder.repairOrderReceived();

        return repository.save(repairOrder);
    }

    private void validateCustomer(CustomerSnapshot customer) {
//        return customerRepository.findById(customer.getId()).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    private void validateVehicle(VehicleSnapshot vehicle) {
//        return vehicleRepository.findById(vehicle.getVehicleId()).orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
    }

}

