package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderPartDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.RepairOrderWorkshopServiceDocument;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.mongo.RepairOrderMongoRepository;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RepairOrderRepositoryImpl implements RepairOrderRepository {

    private final RepairOrderMongoRepository repairOrderMongoRepository;

    public RepairOrderRepositoryImpl(RepairOrderMongoRepository repairOrderMongoRepository) {
        this.repairOrderMongoRepository = repairOrderMongoRepository;
    }

    @Override
    public RepairOrder save(RepairOrder repairOrder) {
        RepairOrderDocument entity = toEntity(repairOrder);
        RepairOrderDocument saved = repairOrderMongoRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsById(String id) {
        return repairOrderMongoRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        repairOrderMongoRepository.deleteById(id);
    }

    @Override
    public Optional<RepairOrder> findById(String id) {
        return repairOrderMongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<RepairOrder> findAll() {
        return repairOrderMongoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RepairOrder> findByStatusContainingIgnoreCase(String status) {
        return repairOrderMongoRepository.findByStatusContainingIgnoreCase(status)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private RepairOrder toDomain(RepairOrderDocument entity) {
        return RepairOrder.builder()
                                .id(entity.getId())
                                .number(entity.getNumber())
                                .status(entity.getStatus())
                                .createdDate(entity.getCreatedDate())
                                .initDate(entity.getInitDate())
                                .finishDate(entity.getFinishDate())
                                .totalServices(entity.getTotalServices())
                                .totalParts(entity.getTotalParts())
                                .total(entity.getTotal())
        //        domain.setCustomer(domain.getCustomer());
        //        domain.setVehicle(domain.getVehicle());
                                .workshopServices(getWorkshopServiceDomain(entity.getWorkshopServices()))
                                .parts(getPartDomain(entity.getParts()))
                                .userId(entity.getUserId())
                                .updatedDate(entity.getUpdatedDate())
                                .build();
    }

    private RepairOrderDocument toEntity(RepairOrder domain) {
        RepairOrderDocument entity = new RepairOrderDocument();
        entity.setId(domain.getId());
        entity.setNumber(domain.getNumber());
        entity.setInitDate(domain.getInitDate());
        entity.setFinishDate(domain.getFinishDate());
        entity.setStatus(domain.getStatus());
        entity.setTotalServices(domain.getTotalServices());
        entity.setTotalParts(domain.getTotalParts());
        entity.setTotal(domain.getTotal());
//        entity.setCustomer(domain.getCustomer());
//        entity.setVehicle(domain.getVehicle());
        entity.setWorkshopServices(getWorkshopServiceDocuments(domain));
        entity.setParts(getPartDocuments(domain.getParts()));
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        return entity;
    }

    private @NotBlank(message = "Parts cannot be blank") List<RepairOrderPartDocument> getPartDocuments(List<PartSnapshot> parts) {
        return parts.stream().map(part -> {
            var partEntity = new RepairOrderPartDocument();
            partEntity.setId(part.getId());
            partEntity.setDescription(part.getDescription());
            partEntity.setCode(part.getCode());
            partEntity.setQuantity(part.getQuantity());
            partEntity.setUnitPrice(part.getUnitPrice());
            return partEntity;
        }).collect(Collectors.toList());
    }

    private static @NonNull List<RepairOrderWorkshopServiceDocument> getWorkshopServiceDocuments(RepairOrder domain) {
        return domain.getWorkshopServices().stream().map(ws -> {
            var wsEntity = new RepairOrderWorkshopServiceDocument();
            wsEntity.setId(ws.getId());
            wsEntity.setDescription(ws.getDescription());
            wsEntity.setQuantity(ws.getQuantity());
            wsEntity.setUnitPrice(ws.getUnitPrice());
            wsEntity.setDurationInMinutes(ws.getDurationInMinutes());
            return wsEntity;
        }).collect(Collectors.toList());
    }

    private @NotBlank(message = "Parts cannot be blank") List<PartSnapshot> getPartDomain(List<RepairOrderPartDocument> parts) {
        if (parts == null) return List.of();
        return parts.stream().map(part -> {
            var partEntity = new PartSnapshot();
            partEntity.setId(part.getId());
            partEntity.setCode(part.getCode());
            partEntity.setDescription(part.getDescription());
            partEntity.setQuantity(part.getQuantity());
            partEntity.setUnitPrice(part.getUnitPrice());
            return partEntity;
        }).collect(Collectors.toList());
    }

    private static @NonNull List<WorkshopServiceSnapshot> getWorkshopServiceDomain(List<RepairOrderWorkshopServiceDocument> domain) {
        if (domain == null) return List.of();
        return domain.stream().map(ws -> {
            var wsEntity = new WorkshopServiceSnapshot();
            wsEntity.setId(ws.getId());
            wsEntity.setDescription(ws.getDescription());
            wsEntity.setUnitPrice(ws.getUnitPrice());
            wsEntity.setQuantity(ws.getQuantity());
            wsEntity.setDurationInMinutes(ws.getDurationInMinutes());
            return wsEntity;
        }).collect(Collectors.toList());
    }

}

