package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseListItemTest {

    @Test
    void shouldExposeAllFields() {
        PurchaseListItem item = new PurchaseListItem("part-1", "Filtro de oleo", 2, 8, new BigDecimal("29.90"));

        assertEquals("part-1", item.getPartId());
        assertEquals("Filtro de oleo", item.getPartName());
        assertEquals(2, item.getCurrentQuantity());
        assertEquals(8, item.getQuantityToBuy());
        assertEquals(new BigDecimal("29.90"), item.getUnitPrice());
    }
}
