package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.dto.PartResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PartMapperTest {

    private final PartMapper partMapper = new PartMapper();

    @Test
    void shouldMapRequestToPart() {
        PartRequest request = new PartRequest("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        Part part = partMapper.requestToPart(request);

        assertNull(part.getId());
        assertEquals("P001", part.getCode());
        assertEquals("Filtro de oleo", part.getName());
        assertEquals(10, part.getQuantity());
        assertEquals(new BigDecimal("29.90"), part.getPrice());
    }

    @Test
    void shouldMapPartToResponse() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        PartResponse response = partMapper.partToResponse(part);

        assertEquals("id-1", response.id());
        assertEquals("P001", response.code());
        assertEquals("Filtro de oleo", response.name());
        assertEquals(10, response.quantity());
        assertEquals(new BigDecimal("29.90"), response.price());
    }
}
