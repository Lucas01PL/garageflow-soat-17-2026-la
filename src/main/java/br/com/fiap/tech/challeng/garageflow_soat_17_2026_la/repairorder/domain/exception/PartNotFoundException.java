package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class PartNotFoundException extends BusinessException {

    public PartNotFoundException(String partId) {
        super(String.format("Part with ID '%s' not found in repair order", partId));
    }

}

