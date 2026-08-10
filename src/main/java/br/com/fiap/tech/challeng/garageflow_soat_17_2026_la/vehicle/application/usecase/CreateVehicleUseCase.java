package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.DuplicateResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public CreateVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle){
        boolean isPresent = vehicleRepository.existsByPlate(vehicle.getPlate());
        if(isPresent){
            log.debug("[DEBUG] - Trying to register duplicated vehicle with plate: {}", vehicle.getPlate());
            throw new DuplicateResourceException("Vehicle", "plate", vehicle.getPlate());
        }

        log.debug("[DEBUG] - POST VEHICLE: {}", vehicle);
        return vehicleRepository.save(vehicle);
    }
}
