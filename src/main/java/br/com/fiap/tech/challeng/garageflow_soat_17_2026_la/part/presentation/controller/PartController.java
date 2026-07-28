package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.DeletePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.GetPartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.UpdateUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.mapper.PartMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.CreatePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/part")
public class PartController {

    private final CreatePartUseCase createPartUseCase;
    private final GetPartUseCase getPartUseCase;
    private final UpdateUseCase updateUseCase;
    private final DeletePartUseCase deletePartUseCase;
    private final PartMapper partMapper;

    public PartController(CreatePartUseCase createPartUseCase, GetPartUseCase getPartUseCase, UpdateUseCase updateUseCase, DeletePartUseCase deletePartUseCase, PartMapper partMapper) {
        this.createPartUseCase = createPartUseCase;
        this.getPartUseCase = getPartUseCase;
        this.updateUseCase = updateUseCase;
        this.deletePartUseCase = deletePartUseCase;
        this.partMapper = partMapper;
    }

    @Operation(
            summary = "Create Auto Parts or Maintenance Supplies.",
            description = "Creates a new auto parts or maintenance supplies."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<PartResponse> createNewPart(@Valid @RequestBody PartRequest request) {
        Part response = createPartUseCase.createPart(partMapper.requestToPart(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(partMapper.partToResponse(response));
    }

    @Operation(
            summary = "Get Auto Parts or Maintenance Supplies.",
            description = "Retrieves an auto part or maintenance supply by its code."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Part/Maintenance Supplies found"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found")
    })
    @GetMapping
    public ResponseEntity<PartResponse> getPartByCode(@Valid @RequestParam String code){
        Optional<Part> partbyCode = getPartUseCase.getPartbyCode(code);
        PartResponse partResponse = partMapper.partToResponse(partbyCode.get());
        return ResponseEntity.status(HttpStatus.OK).body(partResponse);
    }

    @Operation(
            summary = "Update Auto Parts or Maintenance Supplies.",
            description = "Updates an auto part or maintenance supply by its code."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Part/Maintenance Supplies updated"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> updatePart(@Valid @RequestBody PartRequest request, @PathVariable String id) {
        Part updatePartWithId = updateUseCase.updatePartWithId(id, partMapper.requestToPart(request));
        return ResponseEntity.status(HttpStatus.OK).body(partMapper.partToResponse(updatePartWithId));
    }

    @Operation(
            summary = "Delete Auto Parts or Maintenance Supplies.",
            description = "Deletes an auto part or maintenance supply by its code."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Auto Part/Maintenance Supplies deleted"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<PartResponse> deletePart(@PathVariable String id) {
        deletePartUseCase.deletePart(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
