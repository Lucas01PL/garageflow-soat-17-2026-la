package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleResponse;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle requestToVehicle(VehicleRequest vehicleRequest) {
        return new Vehicle(
                vehicleRequest.plate(),
                vehicleRequest.brand(),
                vehicleRequest.model(),
                vehicleRequest.year()
        );
    }

    public VehicleResponse vehicleToResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getPlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear()
        );
    }

}
