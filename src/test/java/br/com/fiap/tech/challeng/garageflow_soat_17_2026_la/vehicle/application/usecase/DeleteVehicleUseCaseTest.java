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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DeleteVehicleUseCase deleteVehicleUseCase;

    @Test
    void shouldDeleteVehicleWhenExists() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        when(vehicleRepository.findById("id-1")).thenReturn(Optional.of(vehicle));

        deleteVehicleUseCase.deleteVehicle("id-1");

        verify(vehicleRepository).delete("id-1");
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenVehicleDoesNotExist() {
        when(vehicleRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deleteVehicleUseCase.deleteVehicle("missing"));
        verify(vehicleRepository, never()).delete(anyString());
    }
}
