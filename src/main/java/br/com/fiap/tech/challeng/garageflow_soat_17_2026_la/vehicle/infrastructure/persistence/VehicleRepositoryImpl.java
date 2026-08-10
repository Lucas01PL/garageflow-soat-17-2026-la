package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehicleMongoRepository vehicleMongoRepository;

    public VehicleRepositoryImpl(VehicleMongoRepository vehicleMongoRepository) {
        this.vehicleMongoRepository = vehicleMongoRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleDocument savedEntity = vehicleMongoRepository.save(toEntity(vehicle));
        return toVehicleDomain(savedEntity);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleMongoRepository.findById(id).map(this::toVehicleDomain);
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return vehicleMongoRepository.findByPlate(plate).map(this::toVehicleDomain);
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleMongoRepository.findAll().
                stream()
                .map(this::toVehicleDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        vehicleMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return vehicleMongoRepository.existsByPlate(plate);
    }

    private Vehicle toVehicleDomain(VehicleDocument vehicleDocument){
        return new Vehicle(
                vehicleDocument.getId(),
                vehicleDocument.getPlate(),
                vehicleDocument.getBrand(),
                vehicleDocument.getModel(),
                vehicleDocument.getYear()
        );
    }

    private VehicleDocument toEntity (Vehicle vehicle){
        VehicleDocument document = new VehicleDocument(
                vehicle.getPlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear()
        );
        document.setId(vehicle.getId());
        return document;
    }

}
