package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class GetVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public GetVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<Vehicle> getVehicleByPlate(String plate) {
        Optional<Vehicle> byPlate = vehicleRepository.findByPlate(plate);
        if(byPlate.isPresent()){
            log.debug("[DEBUG] - GET VEHICLE: {}", byPlate);
            return byPlate;
        }
        throw new ResourceNotFoundException("Vehicle", "plate", plate);
    }

    public Optional<Vehicle> getVehicleById(String id) {
        Optional<Vehicle> byId = vehicleRepository.findById(id);
        if(byId.isPresent()){
            return byId;
        }
        throw new ResourceNotFoundException("Vehicle", "id", id);
    }
}
