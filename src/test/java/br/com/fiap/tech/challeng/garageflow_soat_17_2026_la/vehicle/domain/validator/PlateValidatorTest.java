package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlateValidatorTest {

    @Test
    void shouldAcceptOldFormat() {
        assertTrue(
                PlateValidator.isValid("ABC1234")
        );
    }

    @Test
    void shouldAcceptMercosulFormat() {
        assertTrue(
                PlateValidator.isValid("ABC1D23")
        );
    }

    @Test
    void shouldAcceptPlateWithHyphen() {
        assertTrue(
                PlateValidator.isValid("ABC-1234")
        );
    }

    @Test
    void shouldAcceptLowerCasePlate() {
        assertTrue(
                PlateValidator.isValid("abc1234")
        );
    }

    @Test
    void shouldRejectInvalidPlate() {
        assertFalse(
                PlateValidator.isValid("ABC123")
        );
    }

    @Test
    void shouldRejectNullPlate() {
        assertFalse(
                PlateValidator.isValid(null)
        );
    }

    @Test
    void shouldNormalizePlate() {
        assertEquals(
                "ABC1234",
                PlateValidator.normalize("abc-1234")
        );
    }
}