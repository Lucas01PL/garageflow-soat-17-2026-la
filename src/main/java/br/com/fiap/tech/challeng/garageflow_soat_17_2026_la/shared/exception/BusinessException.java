package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    protected BusinessException(String message) {
        super(message);
    }

}