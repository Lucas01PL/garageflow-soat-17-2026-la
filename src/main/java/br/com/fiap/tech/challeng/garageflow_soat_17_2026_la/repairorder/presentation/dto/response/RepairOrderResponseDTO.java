package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.CustomerSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.VehicleSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.WorkshopServiceSnapshot;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// TODO: utilizar record ao inves de classe
@Data
public class RepairOrderResponseDTO {

    private String id;
    private String number;
    private String status;
    private LocalDateTime initDate;
    private LocalDateTime finishDate;
    private BigDecimal totalServices;
    private BigDecimal totalParts;
    private BigDecimal total;
    private CustomerSnapshot customer;
    private VehicleSnapshot vehicle;
    private List<WorkshopServiceResponse> workshopServices;
    private List<PartSnapshot> parts;
    private String userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}

