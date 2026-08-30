package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.BusinessException;

public class InvalidVehiclePlateException extends BusinessException {

    public InvalidVehiclePlateException(String plate) {
        super("Invalid plate: " + plate);
    }
}