package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.CreateWorkshopServiceRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.WorkshopServiceResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.mapper.WorkshopServiceMapper;
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
@RequestMapping("/workshop-service")
public class WorkshopServiceController {

    private CreateWorkshopServiceUseCase createUseCase;

    private GetWorkshopServiceByIdUseCase getByIdUseCase;

    private UpdateWorkshopServiceUseCase updateUseCase;

    private DeleteWorkshopServiceUseCase deleteUseCase;

    private ListAllWorkshopServicesUseCase listAllUseCase;

    private SearchWorkshopServiceByDescriptionUseCase searchUseCase;

    private WorkshopServiceMapper mapper;

    @Operation(
            summary = "Create Workshop Service",
            description = "Creates a new workshop service."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateWorkshopServiceRequestDTO dto) {
        try {
            WorkshopService svc = mapper.toModel(dto);
            WorkshopService created = createUseCase.execute(svc);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Get Workshop Service by ID",
            description = "Retrieves a workshop service by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service found"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<WorkshopService> svc = getByIdUseCase.execute(id);
            return svc.map(workshopService -> ResponseEntity.ok(mapper.toResponse(workshopService)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "List All Workshop Services",
            description = "Retrieves a list of all workshop services."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Services found")
    })
    @GetMapping
    public ResponseEntity<List<WorkshopServiceResponseDTO>> listAll() {
        List<WorkshopService> list = listAllUseCase.execute();
        List<WorkshopServiceResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Search Workshop Services",
            description = "Searches for workshop services by description."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Services found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String description) {
        try {
            List<WorkshopService> list = searchUseCase.execute(description);
            List<WorkshopServiceResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Update Workshop Service",
            description = "Updates an existing workshop service."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service updated"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody CreateWorkshopServiceRequestDTO dto) {
        try {
            WorkshopService update = mapper.toModel(dto);
            Optional<WorkshopService> updated = updateUseCase.execute(id, update);
            return updated.map(workshopService -> ResponseEntity.ok(mapper.toResponse(workshopService)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete Workshop Service",
            description = "Deletes an existing workshop service."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Service deleted"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean deleted = deleteUseCase.execute(id);
            if (deleted) return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

