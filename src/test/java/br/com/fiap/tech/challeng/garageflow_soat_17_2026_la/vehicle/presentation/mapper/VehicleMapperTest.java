package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VehicleMapperTest {

    private final VehicleMapper vehicleMapper = new VehicleMapper();

    @Test
    void shouldMapRequestToVehicle() {
        VehicleRequest request = new VehicleRequest("ABC1D23", "Volkswagen", "Gol", 2020);

        Vehicle vehicle = vehicleMapper.requestToVehicle(request);

        assertNull(vehicle.getId());
        assertEquals("ABC1D23", vehicle.getPlate());
        assertEquals("Volkswagen", vehicle.getBrand());
        assertEquals("Gol", vehicle.getModel());
        assertEquals(2020, vehicle.getYear());
    }

    @Test
    void shouldMapVehicleToResponse() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        VehicleResponse response = vehicleMapper.vehicleToResponse(vehicle);

        assertEquals("id-1", response.id());
        assertEquals("ABC1D23", response.plate());
        assertEquals("Volkswagen", response.brand());
        assertEquals("Gol", response.model());
        assertEquals(2020, response.year());
    }
}
