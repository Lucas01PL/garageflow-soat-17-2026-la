package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class InvalidPurchaseListStatusException extends BusinessException {
    public InvalidPurchaseListStatusException(String message) {
        super(message);
    }
}
