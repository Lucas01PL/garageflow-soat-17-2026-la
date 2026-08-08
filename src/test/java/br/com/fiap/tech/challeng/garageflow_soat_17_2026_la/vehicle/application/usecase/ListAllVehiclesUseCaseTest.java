package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllVehiclesUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ListAllVehiclesUseCase listAllVehiclesUseCase;

    @Test
    void shouldReturnAllVehicles() {
        Vehicle vehicle1 = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        Vehicle vehicle2 = new Vehicle("id-2", "XYZ9E88", "Fiat", "Uno", 2015);
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<Vehicle> result = listAllVehiclesUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoVehiclesExist() {
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Vehicle> result = listAllVehiclesUseCase.findAll();

        assertTrue(result.isEmpty());
    }
}
