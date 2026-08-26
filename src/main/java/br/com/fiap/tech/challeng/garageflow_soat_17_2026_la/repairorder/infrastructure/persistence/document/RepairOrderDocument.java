package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type.RepairOrderStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Document(collection = "repair_orders")
public class RepairOrderDocument {

    @Id
    private String id;

    private String number;

    private RepairOrderStatus status;

    private LocalDateTime initDate;

    private LocalDateTime finishDate;

    private BigDecimal totalServices;

    private BigDecimal totalParts;

    private BigDecimal total;

    private List<RepairOrderWorkshopServiceDocument> workshopServices;

    private List<RepairOrderPartDocument> parts;

    private RepairOrderCustomerDocument customer;

    private RepairOrderVehicleDocument vehicle;

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}

