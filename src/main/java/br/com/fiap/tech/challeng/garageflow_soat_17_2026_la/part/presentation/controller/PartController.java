package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.mapper.PartMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartResponse;
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
@RequestMapping("/part")
public class PartController {

    private final CreatePartUseCase createPartUseCase;
    private final GetPartUseCase getPartUseCase;
    private final UpdatePartUseCase updatePartUseCase;
    private final DeletePartUseCase deletePartUseCase;
    private final PartMapper partMapper;
    private final ListAllPartsUseCase listAllPartsUseCase;
    private final PartStockControlUseCase partStockControlUseCase;

    public PartController(CreatePartUseCase createPartUseCase, GetPartUseCase getPartUseCase, UpdatePartUseCase updatePartUseCase, DeletePartUseCase deletePartUseCase, PartMapper partMapper, ListAllPartsUseCase listAllPartsUseCase, PartStockControlUseCase partStockControlUseCase) {
        this.createPartUseCase = createPartUseCase;
        this.getPartUseCase = getPartUseCase;
        this.updatePartUseCase = updatePartUseCase;
        this.deletePartUseCase = deletePartUseCase;
        this.partMapper = partMapper;
        this.listAllPartsUseCase = listAllPartsUseCase;
        this.partStockControlUseCase = partStockControlUseCase;
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
            summary = "Debit Auto Parts or Maintenance Supplies stock.",
            description = "Debits a quantity from the stock of an auto part or maintenance supply."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock debited"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found"),
            @ApiResponse(responseCode = "416", description = "Not enough stock to debit")
    })
    @PostMapping("/debit/{id}")
    public ResponseEntity<String> debitPartStock(@PathVariable String id, @RequestParam Integer quantityToDebit) {
        String result = partStockControlUseCase.debitPartStock(id, quantityToDebit);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(
            summary = "Add Auto Parts or Maintenance Supplies stock.",
            description = "Adds a quantity to the stock of an auto part or maintenance supply."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock added"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found"),
            @ApiResponse(responseCode = "416", description = "Quantity to add must be greater than zero")
    })
    @PostMapping("/add/{id}")
    public ResponseEntity<String> addPartStock(@PathVariable String id, @RequestParam Integer quantityToAdd) {
        String result = partStockControlUseCase.addPartStock(id, quantityToAdd);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(
            summary = "Get Auto Parts or Maintenance Supplies.",
            description = "Retrieves an auto part or maintenance supply by its code."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Part/Maintenance Supplies found"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PartResponse> getPartById(@PathVariable String id){
        Optional<Part> partbyCode = getPartUseCase.getPartbyId(id);
        PartResponse partResponse = partMapper.partToResponse(partbyCode.get());
        return ResponseEntity.status(HttpStatus.OK).body(partResponse);
    }

    @Operation(
            summary = "Get Auto Parts or Maintenance Supplies by code.",
            description = "Retrieves an auto part or maintenance supply by its code."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Part/Maintenance Supplies found"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<PartResponse> getPartByCode(@PathVariable String code){
        Optional<Part> partByCode = getPartUseCase.getPartbyCode(code);
        PartResponse partResponse = partMapper.partToResponse(partByCode.get());
        return ResponseEntity.status(HttpStatus.OK).body(partResponse);
    }

    @Operation(
            summary = "List Auto Parts or Maintenance Supplies.",
            description = "Retrieves all auto parts and maintenance supplies."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Parts/Maintenance Supplies listed")
    })
    @GetMapping
    public ResponseEntity<List<PartResponse>> getAllParts(){
        List<Part> allParts = listAllPartsUseCase.findAll();
        List<PartResponse> partResponseList = allParts.stream().map(partMapper::partToResponse).toList();
        return ResponseEntity.status(HttpStatus.OK).body(partResponseList);
    }

    @Operation(
            summary = "Update Auto Parts or Maintenance Supplies.",
            description = "Updates an auto part or maintenance supply by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Part/Maintenance Supplies updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Auto Part/Maintenance Supplies not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> updatePart(@Valid @RequestBody PartRequest request, @PathVariable String id) {
        Part updatePartWithId = updatePartUseCase.updatePartWithId(id, partMapper.requestToPart(request));
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
