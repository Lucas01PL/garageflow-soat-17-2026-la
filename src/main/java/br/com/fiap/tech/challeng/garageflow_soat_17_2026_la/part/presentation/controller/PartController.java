package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.DeletePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.GetPartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.UpdateUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.mapper.PartMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.CreatePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/part")
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

    @PostMapping
    public ResponseEntity<PartResponse> createNewPart(@Valid @RequestBody PartRequest request) {
        Part response = createPartUseCase.createPart(partMapper.requestToPart(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(partMapper.partToResponse(response));
    }

    @GetMapping
    public ResponseEntity<PartResponse> getPartByCode(@Valid @RequestParam String code){
        Optional<Part> partbyCode = getPartUseCase.getPartbyCode(code);
        PartResponse partResponse = partMapper.partToResponse(partbyCode.get());
        return ResponseEntity.status(HttpStatus.OK).body(partResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> updatePart(@Valid @RequestBody PartRequest request, @PathVariable String id) {
        Part updatePartWithId = updateUseCase.updatePartWithId(id, partMapper.requestToPart(request));
        return ResponseEntity.status(HttpStatus.OK).body(partMapper.partToResponse(updatePartWithId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PartResponse> deletePart(@PathVariable String id) {
        deletePartUseCase.deletePart(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
