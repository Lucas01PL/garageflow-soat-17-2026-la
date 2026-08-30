package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(String id);

    Optional<Vehicle> findByPlate(String plate);

    List<Vehicle> findAll();

    void delete(String id);

    boolean existsByPlate(String plate);
}
