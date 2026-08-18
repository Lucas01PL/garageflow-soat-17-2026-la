package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

public class RequiredObjectException extends BusinessException {

    public RequiredObjectException(String objectName) {
        super(String.format("'%s' cannot be null.", objectName));
    }

}