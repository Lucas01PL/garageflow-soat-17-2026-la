package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private UpdateVehicleUseCase updateVehicleUseCase;

    @Test
    void shouldUpdateExistingVehicle() {
        Vehicle existingVehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        Vehicle updatedData = new Vehicle("ABC1D23", "Volkswagen", "Gol GTS", 2021);

        when(vehicleRepository.findById("id-1")).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(existingVehicle)).thenReturn(existingVehicle);

        Vehicle result = updateVehicleUseCase.updateVehicleWithId("id-1", updatedData);

        assertEquals("Gol GTS", result.getModel());
        assertEquals(2021, result.getYear());
        assertEquals("Volkswagen", result.getBrand());

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenVehicleDoesNotExist() {
        Vehicle updatedData = new Vehicle("ABC1D23", "Volkswagen", "Gol GTS", 2021);
        when(vehicleRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateVehicleUseCase.updateVehicleWithId("missing", updatedData));
    }
}
