package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddPartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddWorkshopServiceRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepairOrderMapperTest {

    private final WorkshopServiceResponseMapper workshopServiceMapper = new WorkshopServiceResponseMapper();
    private final RepairOrderMapper mapper = new RepairOrderMapper(workshopServiceMapper);

    @Test
    void toModelShouldMapDtoToModel() {
        CreateRepairOrderRequest dto = new CreateRepairOrderRequest(
                "vehicle1",
                "cust1",
                null,
                null
        );

        RepairOrder model = mapper.toModel(dto, "id1");

        assertNotNull(model);
        assertEquals("vehicle1", model.getVehicle().getVehicleId());
        assertEquals("cust1", model.getCustomer().getCustomerId());
    }

    @Test
    void toModelShouldMapWorkshopServicesAndPartsWithoutCalculatingTotals() {
        AddWorkshopServiceRequest workshopService = new AddWorkshopServiceRequest();
        workshopService.setWorkshopServiceId("service-1");
        workshopService.setQuantity(2);

        AddPartRequest part = new AddPartRequest();
        part.setPartId("part-1");
        part.setQuantity(3);

        CreateRepairOrderRequest dto = new CreateRepairOrderRequest(
                "vehicle1",
                "cust1",
                List.of(workshopService),
                List.of(part));

        RepairOrder model = mapper.toModel(dto, "user1");

        assertEquals("service-1", model.getWorkshopServices().getFirst().getWorkshopServiceId());
        assertEquals(2, model.getWorkshopServices().getFirst().getQuantity());
        assertEquals("part-1", model.getParts().getFirst().getId());
        assertEquals(3, model.getParts().getFirst().getQuantity());
        assertNull(model.getTotalServices());
        assertNull(model.getTotalParts());
    }

    @Test
    void toModelShouldReturnNullWhenDtoIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toResponseShouldMapModelToDto() {
        RepairOrder model = RepairOrder.builder()
                .status(RepairOrderStatus.RECEIVED)
                .total(new BigDecimal("500.00"))
                .userId("user1")
                .id("ro1")
                .build();

        RepairOrderResponseDTO dto = mapper.toResponse(model);

        assertNotNull(dto);
        assertEquals("ro1", dto.getId());
        assertEquals(RepairOrderStatus.RECEIVED.name(), dto.getStatus());
        assertEquals(new BigDecimal("500.00"), dto.getTotal());
        assertEquals("user1", dto.getUserId());
    }

    @Test
    void toResponseShouldReturnNullWhenModelIsNull() {
        assertNull(mapper.toResponse(null));
    }
}
