package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Vehicle {

    private String id;
    private String plate;
    private String brand;
    private String model;
    private Integer year;

    public Vehicle(String plate, String brand, String model, Integer year) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public Vehicle(String id, String plate, String brand, String model, Integer year) {
        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public void update(String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
}
