package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidPurchaseListStatusExceptionTest {

    @Test
    void shouldExposeMessage() {
        InvalidPurchaseListStatusException exception = new InvalidPurchaseListStatusException("Only PENDING purchase lists can be approved. Current status: APPROVED");

        assertEquals("Only PENDING purchase lists can be approved. Current status: APPROVED", exception.getMessage());
    }
}
