package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document;

import lombok.Data;

@Data
public class RepairOrderVehicleDocument {

    private String vehicleId;
    private String plate;
    private String brand;
    private String model;
    private Integer year;
}
