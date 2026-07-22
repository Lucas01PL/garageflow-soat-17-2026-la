package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.CreateWorkshopServiceRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.WorkshopServiceResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.mapper.WorkshopServiceMapper;
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
@RequestMapping("/api/workshopservice")
public class WorkshopServiceController {

    private CreateWorkshopServiceUseCase createUseCase;

    private GetWorkshopServiceByIdUseCase getByIdUseCase;

    private UpdateWorkshopServiceUseCase updateUseCase;

    private DeleteWorkshopServiceUseCase deleteUseCase;

    private ListAllWorkshopServicesUseCase listAllUseCase;

    private SearchWorkshopServiceByDescriptionUseCase searchUseCase;

    private WorkshopServiceMapper mapper;

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

    @GetMapping
    public ResponseEntity<List<WorkshopServiceResponseDTO>> listAll() {
        List<WorkshopService> list = listAllUseCase.execute();
        List<WorkshopServiceResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

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

