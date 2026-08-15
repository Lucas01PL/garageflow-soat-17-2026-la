package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.WorkshopServiceStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@NoArgsConstructor
@Data
public class WorkshopServiceResponse {
    private String workshopServiceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer durationInMinutes;
    private String status;
    private OffsetDateTime startedAt;
    private OffsetDateTime finishedAt;

}
