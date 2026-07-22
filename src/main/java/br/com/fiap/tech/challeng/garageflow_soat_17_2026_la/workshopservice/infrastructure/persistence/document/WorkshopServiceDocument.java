package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Document(collection = "services")
public class WorkshopServiceDocument {

    @Id
    private String id;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Positive(message = "Value must be greater than zero")
    private BigDecimal value;

    public WorkshopServiceDocument(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

}

