package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeleteVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public DeleteVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void deleteVehicle(String id){
        Optional<Vehicle> byId = vehicleRepository.findById(id);
        if(byId.isPresent()){
            log.debug("[DEBUG] - DELETED VEHICLE: {}", byId.get());
            vehicleRepository.delete(id);
            return;
        }
        log.debug("[DEBUG] - Trying to delete non-existant vehicle with id: {}", id);
        throw new ResourceNotFoundException("Vehicle", "id", id);
    }
}
