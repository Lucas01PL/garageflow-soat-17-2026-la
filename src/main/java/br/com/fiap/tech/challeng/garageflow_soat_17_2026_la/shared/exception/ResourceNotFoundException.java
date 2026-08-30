package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format(
                "%s with %s '%s' was not found.",
                resource,
                field,
                value
        ));
    }

}