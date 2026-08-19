package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListItem;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto.PurchaseListResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseListMapperTest {

    private final PurchaseListMapper mapper = new PurchaseListMapper();

    @Test
    void shouldMapDomainToResponse() {
        LocalDateTime now = LocalDateTime.now();
        PurchaseList purchaseList = PurchaseList.builder()
                .id("pl-1")
                .generatedAt(now)
                .status(PurchaseListStatus.PENDING)
                .items(List.of(new PurchaseListItem("part-1", "Filtro de oleo", 2, 8, new BigDecimal("29.90"))))
                .build();

        PurchaseListResponse response = mapper.toResponse(purchaseList);

        assertEquals("pl-1", response.id());
        assertEquals(now, response.generatedAt());
        assertEquals(PurchaseListStatus.PENDING, response.status());
        assertEquals(1, response.items().size());
        assertEquals("part-1", response.items().get(0).partId());
        assertEquals("Filtro de oleo", response.items().get(0).partName());
        assertEquals(2, response.items().get(0).currentQuantity());
        assertEquals(8, response.items().get(0).quantityToBuy());
        assertEquals(new BigDecimal("29.90"), response.items().get(0).unitPrice());
    }
}
