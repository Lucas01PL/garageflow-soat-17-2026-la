package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class InsufficientQuantityException extends BusinessException {

    public InsufficientQuantityException(String itemType, String itemName) {
        super(String.format("Quantity of %s '%s' is insufficient to remove the requested amount.", itemType, itemName));
    }

}

