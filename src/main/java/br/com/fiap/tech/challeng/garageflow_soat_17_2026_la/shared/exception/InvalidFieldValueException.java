package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

public class InvalidFieldValueException extends BusinessException {

    public InvalidFieldValueException(String fieldName, String message) {
        super(String.format("Field '%s' is invalid. '%s'", fieldName, message));
    }

}