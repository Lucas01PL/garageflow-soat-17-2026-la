package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.validator.PlateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public UpdateVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle updateVehicleWithId(String id, Vehicle updatedVehicle) {

        PlateValidator.validate(updatedVehicle.getPlate());

        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));

        existingVehicle.update(
                updatedVehicle.getBrand(),
                updatedVehicle.getModel(),
                updatedVehicle.getYear()
        );

        log.debug("[DEBUG] - UPDATED VEHICLE: {}", existingVehicle);
        return vehicleRepository.save(existingVehicle);
    }
}
