package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleMongoRepository extends MongoRepository<VehicleDocument, String> {
    boolean existsByPlate(String plate);
    Optional<VehicleDocument> findByPlate(String plate);
}
