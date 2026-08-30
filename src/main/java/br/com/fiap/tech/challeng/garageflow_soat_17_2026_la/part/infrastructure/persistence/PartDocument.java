package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.infrastructure.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "parts")
@Getter
@Setter
@NoArgsConstructor
public class PartDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String name;
    private Integer quantity;
    private BigDecimal price;

    public PartDocument(String code, String name, Integer quantity, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

