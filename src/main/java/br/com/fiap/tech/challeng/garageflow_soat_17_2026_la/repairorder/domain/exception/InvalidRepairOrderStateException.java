package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class InvalidRepairOrderStateException extends BusinessException {

    public InvalidRepairOrderStateException(String message) {
        super("Invalid Repair Order State: " + message);
    }

}

