package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.mapper.VehicleMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.presentation.dto.VehicleResponse;
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

    @PostMapping
    public ResponseEntity<VehicleResponse> createNewVehicle(@Valid @RequestBody VehicleRequest request) {
        Vehicle response = createVehicleUseCase.createVehicle(vehicleMapper.requestToVehicle(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleMapper.vehicleToResponse(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@Valid @PathVariable String id){
        Optional<Vehicle> vehicleByPlate = getVehicleUseCase.getVehicleById(id);
        VehicleResponse vehicleResponse = vehicleMapper.vehicleToResponse(vehicleByPlate.get());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResponse);
    }

    @GetMapping("/plate/{plate}")
    public ResponseEntity<VehicleResponse> getVehicleByPlate(@PathVariable String plate){
        Optional<Vehicle> vehicleByPlate = getVehicleUseCase.getVehicleByPlate(plate);
        VehicleResponse vehicleResponse = vehicleMapper.vehicleToResponse(vehicleByPlate.get());
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResponse);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles(){
        List<Vehicle> allVehicles = listAllVehiclesUseCase.findAll();
        List<VehicleResponse> vehicleResponseList = allVehicles.stream().map(vehicleMapper::vehicleToResponse).toList();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleResponseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(@Valid @RequestBody VehicleRequest request, @PathVariable String id) {
        Vehicle updateVehicleWithId = updateUseCase.updateVehicleWithId(id, vehicleMapper.requestToVehicle(request));
        return ResponseEntity.status(HttpStatus.OK).body(vehicleMapper.vehicleToResponse(updateVehicleWithId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VehicleResponse> deleteVehicle(@PathVariable String id) {
        deleteVehicleUseCase.deleteVehicle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
