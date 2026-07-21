package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception;

public class PartNotFoundException extends RuntimeException {
    public PartNotFoundException(String id) {
        super("There is no part with id " + id);
    }
}
