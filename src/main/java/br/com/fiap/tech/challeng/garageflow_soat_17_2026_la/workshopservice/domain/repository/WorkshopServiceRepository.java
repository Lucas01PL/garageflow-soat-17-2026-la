package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;

import java.util.List;
import java.util.Optional;

public interface WorkshopServiceRepository {

    WorkshopService save(WorkshopService service);

    boolean existsById(String id);

    void deleteById(String id);

    Optional<WorkshopService> findById(String id);

    List<WorkshopService> findAll();

    List<WorkshopService> findByDescriptionContainingIgnoreCase(String description);
}
