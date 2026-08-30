package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.CreateWorkshopServiceRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.WorkshopServiceResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.mapper.WorkshopServiceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkshopServiceControllerTest {

    @Mock
    private CreateWorkshopServiceUseCase createUseCase;

    @Mock
    private GetWorkshopServiceByIdUseCase getByIdUseCase;

    @Mock
    private UpdateWorkshopServiceUseCase updateUseCase;

    @Mock
    private DeleteWorkshopServiceUseCase deleteUseCase;

    @Mock
    private ListAllWorkshopServicesUseCase listAllUseCase;

    @Mock
    private SearchWorkshopServiceByDescriptionUseCase searchUseCase;

    @Mock
    private WorkshopServiceMapper mapper;

    @InjectMocks
    private WorkshopServiceController controller;

    @Test
    void createShouldReturnCreatedWhenSuccess() {
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("Desc", new BigDecimal("10"));
        WorkshopService model = new WorkshopService("Desc", new BigDecimal("10"));
        WorkshopService created = new WorkshopService("Desc", new BigDecimal("10"));
        created.setId("1");
        WorkshopServiceResponseDTO resp = new WorkshopServiceResponseDTO("1", "Desc", new BigDecimal("10"));

        when(mapper.toModel(dto)).thenReturn(model);
        when(createUseCase.execute(model)).thenReturn(created);
        when(mapper.toResponse(created)).thenReturn(resp);

        var response = controller.create(dto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(resp, response.getBody());
    }

    @Test
    void createShouldReturnBadRequestWhenUseCaseThrows() {
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("Desc", new BigDecimal("10"));
        WorkshopService model = new WorkshopService("Desc", new BigDecimal("10"));

        when(mapper.toModel(dto)).thenReturn(model);
        when(createUseCase.execute(model)).thenThrow(new InvalidFieldValueException("price", "Price must be greater than zero."));

        assertThrows(InvalidFieldValueException.class, () -> controller.create(dto));
    }

    @Test
    void getByIdShouldReturnOkWhenFound() {
        WorkshopService svc = new WorkshopService("X", new BigDecimal("1"));
        svc.setId("i");
        WorkshopServiceResponseDTO dto = new WorkshopServiceResponseDTO("i", "X", new BigDecimal("1"));

        when(getByIdUseCase.execute("i")).thenReturn(Optional.of(svc));
        when(mapper.toResponse(svc)).thenReturn(dto);

        var response = controller.getById("i");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getByIdShouldReturnNotFoundWhenMissing() {
        when(getByIdUseCase.execute("m")).thenReturn(Optional.empty());

        var response = controller.getById("m");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getByIdShouldReturnBadRequestWhenUseCaseThrows() {
        when(getByIdUseCase.execute(null)).thenThrow(new RequiredFieldException("id"));

        assertThrows(RequiredFieldException.class, () -> controller.getById(null));
    }

    @Test
    void listAllShouldReturnList() {
        WorkshopService s1 = new WorkshopService("A", new BigDecimal("1"));
        s1.setId("1");
        WorkshopServiceResponseDTO d1 = new WorkshopServiceResponseDTO("1", "A", new BigDecimal("1"));

        when(listAllUseCase.execute()).thenReturn(List.of(s1));
        when(mapper.toResponse(s1)).thenReturn(d1);

        var response = controller.list("");

        assertEquals(200, response.getStatusCode().value());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(d1, body.getFirst());
    }

    @Test
    void searchShouldReturnMatches() {
        WorkshopService s = new WorkshopService("Oil", new BigDecimal("5"));
        s.setId("o");
        WorkshopServiceResponseDTO dto = new WorkshopServiceResponseDTO("o", "Oil", new BigDecimal("5"));

        when(searchUseCase.execute("oil")).thenReturn(List.of(s));
        when(mapper.toResponse(s)).thenReturn(dto);

        var response = controller.list("oil");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(dto), response.getBody());
    }

    @Test
    void updateShouldReturnOkWhenUpdated() {
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("New", new BigDecimal("20"));
        WorkshopService update = new WorkshopService("New", new BigDecimal("20"));
        WorkshopService updated = new WorkshopService("New", new BigDecimal("20"));
        updated.setId("u1");
        WorkshopServiceResponseDTO resp = new WorkshopServiceResponseDTO("u1", "New", new BigDecimal("20"));

        when(mapper.toModel(dto)).thenReturn(update);
        when(updateUseCase.execute("u1", update)).thenReturn(Optional.of(updated));
        when(mapper.toResponse(updated)).thenReturn(resp);

        var response = controller.update("u1", dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(resp, response.getBody());
    }

    @Test
    void updateShouldReturnNotFoundWhenMissing() {
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("X", new BigDecimal("1"));
        when(mapper.toModel(dto)).thenReturn(new WorkshopService("X", new BigDecimal("1")));
        when(updateUseCase.execute(eq("no"), any())).thenReturn(Optional.empty());

        var response = controller.update("no", dto);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deleteShouldReturnNoContentWhenDeleted() {
        when(deleteUseCase.execute("d1")).thenReturn(true);

        var response = controller.delete("d1");

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void deleteShouldReturnNotFoundWhenMissing() {
        when(deleteUseCase.execute("x")).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> controller.delete("x"));
    }

    @Test
    void deleteShouldReturnBadRequestWhenUseCaseThrows() {
        when(deleteUseCase.execute(null)).thenThrow(new RequiredFieldException("id"));

        assertThrows(RequiredFieldException.class, () -> controller.delete(null));
    }
}

