package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RepairOrderMapper {

    private final WorkshopServiceResponseMapper workshopServiceMapper;

    public RepairOrder toModel(CreateRepairOrderRequest dto) {
        if (dto == null) return null;
        return RepairOrder.builder()
                .vehicle(new VehicleSnapshot(dto.getVehicleId()))
                .customer(new CustomerSnapshot(dto.getCustomerId()))
                .userId("6a60020e609a66a053aea2f0") // TODO: Remove hardcoded userId and get it from the request or security context
                .build();
    }

    public RepairOrderResponseDTO toResponse(RepairOrder model) {
        if (model == null) return null;
        var repairOrderResponseDTO = new RepairOrderResponseDTO();
        repairOrderResponseDTO.setId(model.getId());
        repairOrderResponseDTO.setNumber(model.getNumber());
        repairOrderResponseDTO.setStatus(model.getStatus().getDescription());
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
}

