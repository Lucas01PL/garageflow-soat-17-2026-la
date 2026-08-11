package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import lombok.Data;

@Data
public class VehicleSnapshot {
    String vehicleId;
    String licensePlate;
    String brand;
    String model;
    Integer manufactureYear;
    Integer modelYear;

    public VehicleSnapshot(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
