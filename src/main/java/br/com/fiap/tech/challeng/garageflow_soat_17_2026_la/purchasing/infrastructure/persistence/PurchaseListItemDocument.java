package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseListItemDocument {
    private String partId;
    private String partName;
    private Integer currentQuantity;
    private Integer quantityToBuy;
    private BigDecimal unitPrice;
}
