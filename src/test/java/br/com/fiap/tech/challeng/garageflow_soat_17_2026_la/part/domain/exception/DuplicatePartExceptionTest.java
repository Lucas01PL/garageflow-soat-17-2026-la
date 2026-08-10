package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicatePartExceptionTest {

    @Test
    void shouldBuildMessageWithCode() {
        DuplicatePartException exception = new DuplicatePartException("P001");

        assertEquals("There is already an part with code P001", exception.getMessage());
    }
}
