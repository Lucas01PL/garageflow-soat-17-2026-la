package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class WorkshopServiceNotFoundException extends BusinessException {

    public WorkshopServiceNotFoundException(String workshopServiceId) {
        super(String.format("Workshop service with ID '%s' not found in repair order", workshopServiceId));
    }

}

