package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class Part {

    private String code;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public void update(String name, Integer quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
