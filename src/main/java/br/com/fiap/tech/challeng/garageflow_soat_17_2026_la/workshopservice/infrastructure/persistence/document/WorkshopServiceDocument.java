package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.infrastructure.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Document(collection = "services")
public class WorkshopServiceDocument {

    @Id
    private String id;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Positive(message = "Value must be greater than zero")
    private BigDecimal value;


    public WorkshopServiceDocument() { }

    public WorkshopServiceDocument(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "WorkshopService{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}

