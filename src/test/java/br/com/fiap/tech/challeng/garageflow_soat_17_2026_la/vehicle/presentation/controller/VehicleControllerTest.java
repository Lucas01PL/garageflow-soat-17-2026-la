package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.CreateVehicleUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.DeleteVehicleUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.GetVehicleUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.ListAllVehiclesUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.UpdateVehicleUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.mapper.VehicleMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private CreateVehicleUseCase createVehicleUseCase;

    @Mock
    private GetVehicleUseCase getVehicleUseCase;

    @Mock
    private UpdateVehicleUseCase updateUseCase;

    @Mock
    private DeleteVehicleUseCase deleteVehicleUseCase;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private ListAllVehiclesUseCase listAllVehiclesUseCase;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void shouldCreateNewVehicle() {
        VehicleRequest request = new VehicleRequest("ABC1D23", "Volkswagen", "Gol", 2020);
        Vehicle vehicle = new Vehicle("ABC1D23", "Volkswagen", "Gol", 2020);
        Vehicle createdVehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        VehicleResponse response = new VehicleResponse("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        when(vehicleMapper.requestToVehicle(request)).thenReturn(vehicle);
        when(createVehicleUseCase.createVehicle(vehicle)).thenReturn(createdVehicle);
        when(vehicleMapper.vehicleToResponse(createdVehicle)).thenReturn(response);

        ResponseEntity<VehicleResponse> result = vehicleController.createNewVehicle(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetVehicleById() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        VehicleResponse response = new VehicleResponse("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        when(getVehicleUseCase.getVehicleById("id-1")).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.vehicleToResponse(vehicle)).thenReturn(response);

        ResponseEntity<VehicleResponse> result = vehicleController.getVehicleById("id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetVehicleByPlate() {
        Vehicle vehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        VehicleResponse response = new VehicleResponse("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);

        when(getVehicleUseCase.getVehicleByPlate("ABC1D23")).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.vehicleToResponse(vehicle)).thenReturn(response);

        ResponseEntity<VehicleResponse> result = vehicleController.getVehicleByPlate("ABC1D23");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAllVehicles() {
        Vehicle vehicle1 = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        Vehicle vehicle2 = new Vehicle("id-2", "XYZ9E88", "Fiat", "Uno", 2015);
        VehicleResponse response1 = new VehicleResponse("id-1", "ABC1D23", "Volkswagen", "Gol", 2020);
        VehicleResponse response2 = new VehicleResponse("id-2", "XYZ9E88", "Fiat", "Uno", 2015);

        when(listAllVehiclesUseCase.findAll()).thenReturn(List.of(vehicle1, vehicle2));
        when(vehicleMapper.vehicleToResponse(vehicle1)).thenReturn(response1);
        when(vehicleMapper.vehicleToResponse(vehicle2)).thenReturn(response2);

        ResponseEntity<List<VehicleResponse>> result = vehicleController.getAllVehicles();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void shouldUpdateVehicle() {
        VehicleRequest request = new VehicleRequest("ABC1D23", "Volkswagen", "Gol GTS", 2021);
        Vehicle mappedVehicle = new Vehicle("ABC1D23", "Volkswagen", "Gol GTS", 2021);
        Vehicle updatedVehicle = new Vehicle("id-1", "ABC1D23", "Volkswagen", "Gol GTS", 2021);
        VehicleResponse response = new VehicleResponse("id-1", "ABC1D23", "Volkswagen", "Gol GTS", 2021);

        when(vehicleMapper.requestToVehicle(request)).thenReturn(mappedVehicle);
        when(updateUseCase.updateVehicleWithId("id-1", mappedVehicle)).thenReturn(updatedVehicle);
        when(vehicleMapper.vehicleToResponse(updatedVehicle)).thenReturn(response);

        ResponseEntity<VehicleResponse> result = vehicleController.updateVehicle(request, "id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldDeleteVehicle() {
        ResponseEntity<VehicleResponse> result = vehicleController.deleteVehicle("id-1");

        assertEquals(204, result.getStatusCode().value());
    }
}
