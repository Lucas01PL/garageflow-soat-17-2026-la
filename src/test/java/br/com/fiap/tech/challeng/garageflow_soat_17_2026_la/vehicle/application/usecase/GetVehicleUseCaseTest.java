package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private GetVehicleUseCase getVehicleUseCase;

    @Test
    void shouldReturnVehicleWhenPlateExists() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        when(vehicleRepository.findByPlate("ABC1D23")).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = getVehicleUseCase.getVehicleByPlate("ABC1D23");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenPlateDoesNotExist() {
        when(vehicleRepository.findByPlate("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getVehicleUseCase.getVehicleByPlate("missing"));
    }

    @Test
    void shouldReturnVehicleWhenIdExists() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        when(vehicleRepository.findById("id-1")).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = getVehicleUseCase.getVehicleById("id-1");

        assertTrue(result.isPresent());
        assertEquals("ABC1D23", result.get().getPlate());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(vehicleRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getVehicleUseCase.getVehicleById("missing"));
    }
}
