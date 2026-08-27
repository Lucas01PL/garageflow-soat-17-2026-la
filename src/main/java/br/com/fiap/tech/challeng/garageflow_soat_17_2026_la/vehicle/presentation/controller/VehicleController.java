package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.mapper.VehicleMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleUseCase getVehicleUseCase;
    private final UpdateVehicleUseCase updateUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    private final VehicleMapper vehicleMapper;
    private final ListAllVehiclesUseCase listAllVehiclesUseCase;

    public VehicleController(CreateVehicleUseCase createVehicleUseCase, GetVehicleUseCase getVehicleUseCase, UpdateVehicleUseCase updateUseCase, DeleteVehicleUseCase deleteVehicleUseCase, VehicleMapper vehicleMapper, ListAllVehiclesUseCase listAllVehiclesUseCase) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.getVehicleUseCase = getVehicleUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
        this.vehicleMapper = vehicleMapper;
        this.listAllVehiclesUseCase = listAllVehiclesUseCase;
    }

    @Operation(
            summary = "Create Vehicle.",
            description = "Creates a new vehicle."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicle created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Vehicle with the given plate already exists")
    })
    @PostMapping
    public ResponseEntity<VehicleResponse> createNewVehicle(@Valid @RequestBody VehicleRequest request) {
        Vehicle response = createVehicleUseCase.createVehicle(vehicleMapper.requestToVehicle(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleMapper.vehicleToResponse(response));
    }

    @Operation(
            summary = "Get Vehicle by id.",
            description = "Retrieves a vehicle by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle found"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@Valid @PathVariable String id){
        Optional<Vehicle> vehicleByPlate = getVehicleUseCase.getVehicleById(id);
        VehicleResponse vehicleResponse = vehicleMapper.vehicleToResponse(vehicleByPlate.get());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResponse);
    }

    @Operation(
            summary = "List Vehicles.",
            description = "List vehicles optionally filtered by plate. If no plate is provided, all vehicles are returned."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle found")
    })
    @GetMapping
    public ResponseEntity<List<VehicleResponse>> listVehicles(@RequestParam String plate){
        List<Vehicle> vehicles;
        if (plate == null || plate.isBlank()) {
            vehicles = listAllVehiclesUseCase.findAll();
        } else {
            vehicles = getVehicleUseCase.getVehicleByPlate(plate).stream().toList();
        }
        List<VehicleResponse> vehicleResponses = vehicles.stream().map(vehicleMapper::vehicleToResponse).toList();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResponses);
    }

    @Operation(
            summary = "Update Vehicle.",
            description = "Updates a vehicle by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(@Valid @RequestBody VehicleRequest request, @PathVariable String id) {
        Vehicle updateVehicleWithId = updateUseCase.updateVehicleWithId(id, vehicleMapper.requestToVehicle(request));
        return ResponseEntity.status(HttpStatus.OK).body(vehicleMapper.vehicleToResponse(updateVehicleWithId));
    }

    @Operation(
            summary = "Delete Vehicle.",
            description = "Deletes a vehicle by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle deleted"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<VehicleResponse> deleteVehicle(@PathVariable String id) {
        deleteVehicleUseCase.deleteVehicle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
