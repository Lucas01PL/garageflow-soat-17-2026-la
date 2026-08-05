package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

}