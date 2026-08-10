package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.validation;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidDocumentException;

public final class CpfCnpjValidator {

    private static final int[] CNPJ_FIRST_WEIGHTS = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] CNPJ_SECOND_WEIGHTS = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private CpfCnpjValidator() {
    }

    public static String validateAndNormalize(String document) {
        String digits = document == null ? "" : document.replaceAll("\\D", "");

        boolean isValid = switch (digits.length()) {
            case 11 -> isValidCpf(digits);
            case 14 -> isValidCnpj(digits);
            default -> false;
        };

        if (!isValid) {
            throw new InvalidDocumentException(document);
        }

        return digits;
    }

    private static boolean isValidCpf(String cpf) {
        if (hasAllSameDigits(cpf)) {
            return false;
        }

        int firstCheckDigit = calculateCheckDigit(cpf, 9, 10);
        int secondCheckDigit = calculateCheckDigit(cpf, 10, 11);

        return firstCheckDigit == digitAt(cpf, 9) && secondCheckDigit == digitAt(cpf, 10);
    }

    private static boolean isValidCnpj(String cnpj) {
        if (hasAllSameDigits(cnpj)) {
            return false;
        }

        int firstCheckDigit = calculateCheckDigit(cnpj, CNPJ_FIRST_WEIGHTS);
        int secondCheckDigit = calculateCheckDigit(cnpj, CNPJ_SECOND_WEIGHTS);

        return firstCheckDigit == digitAt(cnpj, 12) && secondCheckDigit == digitAt(cnpj, 13);
    }

    private static int calculateCheckDigit(String digits, int length, int weightStart) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += digitAt(digits, i) * (weightStart - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private static int calculateCheckDigit(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += digitAt(digits, i) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private static int digitAt(String digits, int index) {
        return digits.charAt(index) - '0';
    }

    private static boolean hasAllSameDigits(String digits) {
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }
}
