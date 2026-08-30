package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.document.WorkshopServiceDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.mongo.WorkshopServiceMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WorkshopServiceRepositoryImpl implements WorkshopServiceRepository {

    private final WorkshopServiceMongoRepository workshopServiceMongoRepository;

    public WorkshopServiceRepositoryImpl(WorkshopServiceMongoRepository workshopServiceMongoRepository) {
        this.workshopServiceMongoRepository = workshopServiceMongoRepository;
    }

    @Override
    public WorkshopService save(WorkshopService service) {
        WorkshopServiceDocument entity = toEntity(service);
        WorkshopServiceDocument saved = workshopServiceMongoRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsById(String id) {
        return workshopServiceMongoRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        workshopServiceMongoRepository.deleteById(id);
    }

    @Override
    public Optional<WorkshopService> findById(String id) {
        return workshopServiceMongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<WorkshopService> findAll() {
        return workshopServiceMongoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkshopService> findByDescriptionContainingIgnoreCase(String description) {
        return workshopServiceMongoRepository.findByDescriptionContainingIgnoreCase(description)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private WorkshopService toDomain(WorkshopServiceDocument entity){
        WorkshopService domain = new WorkshopService();
        domain.setId(entity.getId());
        domain.setDescription(entity.getDescription());
        domain.setPrice(entity.getPrice());
        return domain;
    }

    private WorkshopServiceDocument toEntity(WorkshopService domain){
        WorkshopServiceDocument entity = new WorkshopServiceDocument();
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setId(domain.getId());
        return entity;
    }

}
