package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class WorkshopServiceResponse {
    private String workshopServiceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer durationInMinutes;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

}
