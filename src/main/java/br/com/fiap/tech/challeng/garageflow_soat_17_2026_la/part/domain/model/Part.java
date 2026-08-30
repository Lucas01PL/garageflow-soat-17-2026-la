package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model;


import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class Part {

    private String id;
    private String code;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public Part(String code, String name, Integer quantity, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Part(String id, String code, String name, Integer quantity, BigDecimal price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void update(String name, Integer quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
