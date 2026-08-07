package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.CreatePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.DeletePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.GetPartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.ListAllPartsUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.UpdatePartUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.mapper.PartMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartControllerTest {

    @Mock
    private CreatePartUseCase createPartUseCase;

    @Mock
    private GetPartUseCase getPartUseCase;

    @Mock
    private UpdatePartUseCase updatePartUseCase;

    @Mock
    private DeletePartUseCase deletePartUseCase;

    @Mock
    private PartMapper partMapper;

    @Mock
    private ListAllPartsUseCase listAllPartsUseCase;

    @Mock
    private PartStockControlUseCase partStockControlUseCase;

    @InjectMocks
    private PartController partController;

    @Test
    void shouldCreateNewPart() {
        PartRequest request = new PartRequest("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        Part part = new Part("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        Part createdPart = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        PartResponse response = new PartResponse("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        when(partMapper.requestToPart(request)).thenReturn(part);
        when(createPartUseCase.createPart(part)).thenReturn(createdPart);
        when(partMapper.partToResponse(createdPart)).thenReturn(response);

        ResponseEntity<PartResponse> result = partController.createNewPart(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldDebitPartStock() {
        when(partStockControlUseCase.debitPartStock("id-1", 5)).thenReturn("Debited successfully");

        ResponseEntity<String> result = partController.debitPartStock("id-1", 5);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Debited successfully", result.getBody());
    }

    @Test
    void shouldAddPartStock() {
        when(partStockControlUseCase.addPartStock("id-1", 5)).thenReturn("Added successfully");

        ResponseEntity<String> result = partController.addPartStock("id-1", 5);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Added successfully", result.getBody());
    }

    @Test
    void shouldGetPartById() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        PartResponse response = new PartResponse("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        when(getPartUseCase.getPartbyId("id-1")).thenReturn(Optional.of(part));
        when(partMapper.partToResponse(part)).thenReturn(response);

        ResponseEntity<PartResponse> result = partController.getPartById("id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAllParts() {
        Part part1 = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        Part part2 = new Part("id-2", "P002", "Pastilha de freio", 5, new BigDecimal("89.90"));
        PartResponse response1 = new PartResponse("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        PartResponse response2 = new PartResponse("id-2", "P002", "Pastilha de freio", 5, new BigDecimal("89.90"));

        when(listAllPartsUseCase.findAll()).thenReturn(List.of(part1, part2));
        when(partMapper.partToResponse(part1)).thenReturn(response1);
        when(partMapper.partToResponse(part2)).thenReturn(response2);

        ResponseEntity<List<PartResponse>> result = partController.getAllParts();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void shouldUpdatePart() {
        PartRequest request = new PartRequest("P001", "Filtro de oleo premium", 20, new BigDecimal("39.90"));
        Part mappedPart = new Part("P001", "Filtro de oleo premium", 20, new BigDecimal("39.90"));
        Part updatedPart = new Part("id-1", "P001", "Filtro de oleo premium", 20, new BigDecimal("39.90"));
        PartResponse response = new PartResponse("id-1", "P001", "Filtro de oleo premium", 20, new BigDecimal("39.90"));

        when(partMapper.requestToPart(request)).thenReturn(mappedPart);
        when(updatePartUseCase.updatePartWithId("id-1", mappedPart)).thenReturn(updatedPart);
        when(partMapper.partToResponse(updatedPart)).thenReturn(response);

        ResponseEntity<PartResponse> result = partController.updatePart(request, "id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldDeletePart() {
        ResponseEntity<PartResponse> result = partController.deletePart("id-1");

        assertEquals(204, result.getStatusCode().value());
    }
}
