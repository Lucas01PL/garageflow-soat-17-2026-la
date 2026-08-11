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

    @Positive(message = "Number must be greater than zero")
    private String number;

    @NotBlank(message = "Status cannot be blank")
    private RepairOrderStatus status;

    private LocalDateTime initDate;

    private LocalDateTime finishDate;

    @Positive(message = "Total services must be greater than zero")
    private BigDecimal totalServices;

    @Positive(message = "Total parts must be greater than zero")
    private BigDecimal totalParts;

    @Positive(message = "Total must be greater than zero")
    private BigDecimal total;

    @NotBlank(message = "Workshop Services cannot be blank")
    private List<RepairOrderWorkshopServiceDocument> workshopServices;

    @NotBlank(message = "Parts cannot be blank")
    private List<RepairOrderPartDocument> parts;

    @NotBlank(message = "Customer cannot be blank")
    private RepairOrderCustomerDocument customer;

    @NotBlank(message = "Customer cannot be blank")
    private RepairOrderVehicleDocument vehicle;

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}

