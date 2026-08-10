package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.DuplicateResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private CreateVehicleUseCase createVehicleUseCase;

    @Test
    void shouldCreateVehicleWhenPlateIsNotDuplicated() {
        Vehicle vehicle = new Vehicle("ABC1D23", "Volkswagen", "Gol", 2020);
        Vehicle saved = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        when(vehicleRepository.existsByPlate("ABC1D23")).thenReturn(false);
        when(vehicleRepository.save(vehicle)).thenReturn(saved);

        Vehicle result = createVehicleUseCase.createVehicle(vehicle);

        assertNotNull(result);
        assertEquals("id-1", result.getId());
        assertEquals("ABC1D23", result.getPlate());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenPlateAlreadyExists() {
        Vehicle vehicle = new Vehicle("ABC1D23", "Volkswagen", "Gol", 2020);

        when(vehicleRepository.existsByPlate("ABC1D23")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> createVehicleUseCase.createVehicle(vehicle));
        verify(vehicleRepository, never()).save(any());
    }
}
