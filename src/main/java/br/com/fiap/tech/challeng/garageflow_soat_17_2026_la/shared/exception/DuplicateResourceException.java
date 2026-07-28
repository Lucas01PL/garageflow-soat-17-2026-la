package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format(
                "%s with %s '%s' already exists.",
                resource,
                field,
                value
        ));
    }

}