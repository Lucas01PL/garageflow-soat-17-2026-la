package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class InvalidRepairOrderItemException extends BusinessException {

    public InvalidRepairOrderItemException(String message) {
        super("Invalid Repair Order Item: " + message);
    }

}

