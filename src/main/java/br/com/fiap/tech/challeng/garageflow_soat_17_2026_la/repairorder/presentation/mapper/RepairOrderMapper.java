package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.CreateRepairOrderResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RepairOrderMapper {

    private final WorkshopServiceResponseMapper workshopServiceMapper;

    public RepairOrder toModel(CreateRepairOrderRequest dto) {
        return toModel(dto, null);
    }

    public RepairOrder toModel(CreateRepairOrderRequest dto, String userId) {
        if (dto == null) return null;

        if (userId == null || userId.isBlank()) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        RepairOrder repairOrder = RepairOrder.builder()
                .vehicle(new VehicleSnapshot(dto.getVehicleId()))
                .customer(new CustomerSnapshot(dto.getCustomerId()))
                .userId(userId)
                .workshopServices(dto.getWorkshopServices() == null ? List.of() : dto.getWorkshopServices().stream()
                        .map(service -> service == null ? null : new WorkshopServiceSnapshot(
                                service.getWorkshopServiceId(),
                                service.getQuantity()))
                        .toList())
                .parts(dto.getParts() == null ? List.of() : dto.getParts().stream()
                        .map(part -> part == null ? null : new PartSnapshot(
                                part.getPartId(),
                                part.getQuantity()))
                        .toList())
                .build();

        return repairOrder;
    }

    public RepairOrderResponseDTO toResponse(RepairOrder model) {
        if (model == null) return null;
        var repairOrderResponseDTO = new RepairOrderResponseDTO();
        repairOrderResponseDTO.setId(model.getId());
        repairOrderResponseDTO.setNumber(model.getNumber());
        repairOrderResponseDTO.setStatus(model.getStatus().name());
        repairOrderResponseDTO.setInitDate(model.getInitDate());
        repairOrderResponseDTO.setFinishDate(model.getFinishDate());
        repairOrderResponseDTO.setTotalServices(model.getTotalServices());
        repairOrderResponseDTO.setTotalParts(model.getTotalParts());
        repairOrderResponseDTO.setTotal(model.getTotal());
        repairOrderResponseDTO.setCustomer(model.getCustomer());
        repairOrderResponseDTO.setVehicle(model.getVehicle());
        repairOrderResponseDTO.setWorkshopServices(model.getWorkshopServices().stream().map(workshopServiceMapper::toResponse).toList());
        repairOrderResponseDTO.setParts(model.getParts());
        repairOrderResponseDTO.setUserId(model.getUserId());
        repairOrderResponseDTO.setCreatedDate(model.getCreatedDate());
        repairOrderResponseDTO.setUpdatedDate(model.getUpdatedDate());
        return repairOrderResponseDTO;
    }

    public CreateRepairOrderResponse toCreateRepairOrderResponse(RepairOrder model) {
        if (model == null) return null;

        return new CreateRepairOrderResponse(model.getId());
    }
}
