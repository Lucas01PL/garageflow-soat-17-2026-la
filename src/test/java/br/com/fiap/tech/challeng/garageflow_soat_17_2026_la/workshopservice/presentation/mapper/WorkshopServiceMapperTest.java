package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.CreateWorkshopServiceRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.WorkshopServiceResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WorkshopServiceMapperTest {

    private final WorkshopServiceMapper mapper = new WorkshopServiceMapper();

    @Test
    void toModelShouldMapDtoToModel() {
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("Test desc", new BigDecimal("10"));

        WorkshopService model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals("Test desc", model.getDescription());
        assertEquals(new BigDecimal("10"), model.getPrice());
    }

    @Test
    void toModelShouldReturnNullWhenDtoIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toResponseShouldMapModelToDto() {
        WorkshopService model = new WorkshopService("Desc", new BigDecimal("5"));
        model.setId("i1");

        WorkshopServiceResponseDTO dto = mapper.toResponse(model);

        assertNotNull(dto);
        assertEquals("i1", dto.getId());
        assertEquals("Desc", dto.getDescription());
        assertEquals(new BigDecimal("5"), dto.getValue());
    }

    @Test
    void toResponseShouldReturnNullWhenModelIsNull() {
        assertNull(mapper.toResponse(null));
    }
}

