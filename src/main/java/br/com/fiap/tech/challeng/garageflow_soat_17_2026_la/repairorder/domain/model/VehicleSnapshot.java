package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import lombok.Data;

@Data
public class VehicleSnapshot {
    private String vehicleId;
    private String plate;
    private String brand;
    private String model;
    private Integer year;

    public VehicleSnapshot(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public static VehicleSnapshot from(Vehicle vehicle) {
        VehicleSnapshot snapshot = new VehicleSnapshot(vehicle.getId());
        snapshot.setPlate(vehicle.getPlate());
        snapshot.setBrand(vehicle.getBrand());
        snapshot.setModel(vehicle.getModel());
        snapshot.setYear(vehicle.getYear());
        return snapshot;
    }
}
