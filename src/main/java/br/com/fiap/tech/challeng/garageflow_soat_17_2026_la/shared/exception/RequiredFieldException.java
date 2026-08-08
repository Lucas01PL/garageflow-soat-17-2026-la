package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

public class RequiredFieldException extends BusinessException {

    public RequiredFieldException(String fieldName) {
        super(String.format("Field '%s' is required.", fieldName));
    }

}