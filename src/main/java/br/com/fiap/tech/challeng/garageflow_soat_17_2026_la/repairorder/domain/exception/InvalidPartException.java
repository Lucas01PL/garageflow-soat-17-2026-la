package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class InvalidPartException extends BusinessException {

    public InvalidPartException(String message) {
        super("Invalid Part: " + message);
    }

}

