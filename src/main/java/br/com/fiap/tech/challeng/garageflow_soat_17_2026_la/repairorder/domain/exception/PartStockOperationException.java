package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class PartStockOperationException extends BusinessException {

    public PartStockOperationException(String operation, String message) {
        super(String.format("Failed to %s part stock: %s", operation, message));
    }

}

