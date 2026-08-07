package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.infrastructure.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class VehicleDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String plate;

    private String brand;
    private String model;
    private Integer year;

    public VehicleDocument(String plate, String brand, String model, Integer year) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
}
