package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddPartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddWorkshopServiceRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.RepairOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/repairorder")
public class RepairOrderController {

    private CreateRepairOrderUseCase createUseCase;

    private GetRepairOrderByIdUseCase getByIdUseCase;

    private ListAllRepairOrdersUseCase listAllUseCase;

    private AddWorkshopServiceUseCase addWorkshopServiceUseCase;

    private AddPartUseCase addPartUseCase;

    private RepairOrderMapper mapper;

    @Operation(
            summary = "Create Repair Order",
            description = "Creates a new repair order."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Repair order created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRepairOrderRequest dto) {
        try {
            RepairOrder ro = mapper.toModel(dto);
            RepairOrder created = createUseCase.execute(ro);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Get Repair Order by ID",
            description = "Retrieves a repair order by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Repair order found"),
            @ApiResponse(responseCode = "404", description = "Repair order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<RepairOrder> ro = getByIdUseCase.execute(id);
            return ro.map(repairOrder -> ResponseEntity.ok(mapper.toResponse(repairOrder)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "List All Repair Orders",
            description = "Retrieves a list of all repair orders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Repair orders found")
    })
    @GetMapping
    public ResponseEntity<List<RepairOrderResponseDTO>> listAll() {
        List<RepairOrder> list = listAllUseCase.execute();
        List<RepairOrderResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Add Workshop Service to Repair Order",
            description = "Adds a workshop service to an existing repair order."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workshop service added")
    })
    @PostMapping("/{repairOrderId}/services")
    public ResponseEntity<?> addWorkshopService(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddWorkshopServiceRequest request) {

        RepairOrder repairOrder =
                addWorkshopServiceUseCase.execute(repairOrderId, request);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Add Part to Repair Order",
            description = "Adds a part to an existing repair order."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Part added")
    })
    @PostMapping("/{repairOrderId}/parts")
    public ResponseEntity<?> addPart(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddPartRequest request) {

        RepairOrder repairOrder =
                addPartUseCase.execute(repairOrderId, request);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }
}

