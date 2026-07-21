package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception;

public class DuplicatePartException extends RuntimeException {
    public DuplicatePartException(String code) {
        super("There is already an part with code " + code);
    }
}
