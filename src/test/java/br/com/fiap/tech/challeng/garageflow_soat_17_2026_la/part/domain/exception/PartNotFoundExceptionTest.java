package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartNotFoundExceptionTest {

    @Test
    void shouldBuildMessageWithId() {
        PartNotFoundException exception = new PartNotFoundException("id-1");

        assertEquals("There is no part with id id-1", exception.getMessage());
    }
}
