package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.validation;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidDocumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CpfCnpjValidatorTest {

    @Test
    void shouldValidateAndNormalizeValidCpfWithPunctuation() {
        String result = CpfCnpjValidator.validateAndNormalize("529.982.247-25");

        assertEquals("52998224725", result);
    }

    @Test
    void shouldValidateAndNormalizeValidCpfWithoutPunctuation() {
        String result = CpfCnpjValidator.validateAndNormalize("52998224725");

        assertEquals("52998224725", result);
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenCpfCheckDigitIsWrong() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize("52998224700"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenCpfHasAllSameDigits() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize("11111111111"));
    }

    @Test
    void shouldValidateAndNormalizeValidCnpjWithPunctuation() {
        String result = CpfCnpjValidator.validateAndNormalize("11.222.333/0001-81");

        assertEquals("11222333000181", result);
    }

    @Test
    void shouldValidateAndNormalizeValidCnpjWithoutPunctuation() {
        String result = CpfCnpjValidator.validateAndNormalize("11222333000181");

        assertEquals("11222333000181", result);
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenCnpjCheckDigitIsWrong() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize("11222333000199"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenCnpjHasAllSameDigits() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize("11111111111111"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenLengthIsInvalid() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize("123456"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenDocumentIsNull() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize(null));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenDocumentIsEmpty() {
        assertThrows(InvalidDocumentException.class, () -> CpfCnpjValidator.validateAndNormalize(""));
    }
}
