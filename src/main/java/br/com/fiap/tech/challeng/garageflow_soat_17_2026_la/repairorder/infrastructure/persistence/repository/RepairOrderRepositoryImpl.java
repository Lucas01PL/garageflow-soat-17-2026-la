package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document.*;
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

    @Override
    public List<RepairOrder> findByCustomerId(String customerId) {
        return repairOrderMongoRepository
                .findByCustomer_CustomerId(customerId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByPartIdAndStatusIn(
            String partId,
            List<RepairOrderStatus> statuses) {

        return repairOrderMongoRepository
                .existsByPartIdAndStatusIn(
                        statuses,
                        partId
                );
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
                                .customer(getCustomerDomain(entity.getCustomer()))
                                .vehicle(getVehicleDomain(entity.getVehicle()))
                                .workshopServices(getWorkshopServiceDomain(entity.getWorkshopServices()))
                                .parts(getPartDomain(entity.getParts()))
                                .userId(entity.getUserId())
                                .updatedDate(entity.getUpdatedDate())
                                .build();
    }

    private VehicleSnapshot getVehicleDomain(@NotBlank(message = "Customer cannot be blank") RepairOrderVehicleDocument vehicle) {
        if (vehicle == null) return null;
        VehicleSnapshot vehicleSnapshot = new VehicleSnapshot(vehicle.getVehicleId());
        vehicleSnapshot.setBrand(vehicle.getBrand());
        vehicleSnapshot.setModel(vehicle.getModel());
        vehicleSnapshot.setYear(vehicle.getYear());
        vehicleSnapshot.setPlate(vehicle.getPlate());
        return vehicleSnapshot;
    }

    private CustomerSnapshot getCustomerDomain(@NotBlank(message = "Customer cannot be blank") RepairOrderCustomerDocument customer) {
        if (customer == null) return null;
        CustomerSnapshot customerSnapshot = new CustomerSnapshot(customer.getCustomerId());
        customerSnapshot.setName(customer.getName());
        customerSnapshot.setDocument(customer.getDocument());
        customerSnapshot.setPhone(customer.getPhone());
        customerSnapshot.setEmail(customer.getEmail());
        customerSnapshot.setAddress(customer.getAddress());
        return customerSnapshot;
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
        entity.setCustomer(getCustomerDocument(domain.getCustomer()));
        entity.setVehicle(getVehicleDocument(domain.getVehicle()));
        entity.setWorkshopServices(getWorkshopServiceDocuments(domain));
        entity.setParts(getPartDocuments(domain.getParts()));
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setUpdatedDate(domain.getUpdatedDate());
        return entity;
    }

    private @NotBlank(message = "Customer cannot be blank") RepairOrderVehicleDocument getVehicleDocument(VehicleSnapshot vehicle) {
        if (vehicle == null) return null;
        var vehicleDocument = new RepairOrderVehicleDocument();
        vehicleDocument.setVehicleId(vehicle.getVehicleId());
        vehicleDocument.setBrand(vehicle.getBrand());
        vehicleDocument.setModel(vehicle.getModel());
        vehicleDocument.setYear(vehicle.getYear());
        vehicleDocument.setPlate(vehicle.getPlate());
        return vehicleDocument;
    }

    private @NotBlank(message = "Customer cannot be blank") RepairOrderCustomerDocument getCustomerDocument(CustomerSnapshot customer) {
        if (customer == null) return null;
        var customerDocument = new RepairOrderCustomerDocument();
        customerDocument.setCustomerId(customer.getCustomerId());
        customerDocument.setName(customer.getName());
        customerDocument.setDocument(customer.getDocument());
        customerDocument.setPhone(customer.getPhone());
        customerDocument.setEmail(customer.getEmail());
        customerDocument.setAddress(customer.getAddress());
        return customerDocument;
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
            wsEntity.setWorkshopServiceId(ws.getWorkshopServiceId());
            wsEntity.setDescription(ws.getDescription());
            wsEntity.setQuantity(ws.getQuantity());
            wsEntity.setUnitPrice(ws.getUnitPrice());
            wsEntity.setDurationInMinutes(ws.getDurationInMinutes());
            wsEntity.setStatus(ws.getStatus());
            wsEntity.setStartedAt(ws.getStartedAt());
            wsEntity.setFinishedAt(ws.getFinishedAt());
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
            wsEntity.setWorkshopServiceId(ws.getWorkshopServiceId());
            wsEntity.setDescription(ws.getDescription());
            wsEntity.setUnitPrice(ws.getUnitPrice());
            wsEntity.setQuantity(ws.getQuantity());
            wsEntity.setDurationInMinutes(ws.getDurationInMinutes());
            wsEntity.setStatus(ws.getStatus());
            wsEntity.setStartedAt(ws.getStartedAt());
            wsEntity.setFinishedAt(ws.getFinishedAt());
            return wsEntity;
        }).collect(Collectors.toList());
    }

}

