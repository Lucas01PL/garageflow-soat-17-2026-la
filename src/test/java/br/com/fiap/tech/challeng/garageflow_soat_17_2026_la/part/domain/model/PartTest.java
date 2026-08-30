package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartTest {

    @Test
    void shouldCreatePartWithoutId() {
        Part part = new Part("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        assertNull(part.getId());
        assertEquals("P001", part.getCode());
        assertEquals("Filtro de oleo", part.getName());
        assertEquals(10, part.getQuantity());
        assertEquals(new BigDecimal("29.90"), part.getPrice());
    }

    @Test
    void shouldCreatePartWithId() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        assertEquals("id-1", part.getId());
        assertEquals("P001", part.getCode());
    }

    @Test
    void shouldUpdateNameQuantityAndPrice() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        part.update("Filtro de oleo premium", 20, new BigDecimal("39.90"));

        assertEquals("Filtro de oleo premium", part.getName());
        assertEquals(20, part.getQuantity());
        assertEquals(new BigDecimal("39.90"), part.getPrice());
        assertEquals("id-1", part.getId());
        assertEquals("P001", part.getCode());
    }

    @Test
    void shouldUpdateOnlyQuantity() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        part.updateQuantity(5);

        assertEquals(5, part.getQuantity());
        assertEquals("Filtro de oleo", part.getName());
        assertEquals(new BigDecimal("29.90"), part.getPrice());
    }

    @Test
    void toStringShouldContainFieldValues() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        assertTrue(part.toString().contains("P001"));
        assertTrue(part.toString().contains("Filtro de oleo"));
    }
}
