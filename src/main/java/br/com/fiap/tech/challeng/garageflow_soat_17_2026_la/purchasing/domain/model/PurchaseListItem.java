package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PurchaseListItem {

    private final String partId;
    private final String partName;
    private final Integer currentQuantity;
    private final Integer quantityToBuy;
    private final BigDecimal unitPrice;

    public PurchaseListItem(String partId, String partName, Integer currentQuantity, Integer quantityToBuy, BigDecimal unitPrice) {
        this.partId = partId;
        this.partName = partName;
        this.currentQuantity = currentQuantity;
        this.quantityToBuy = quantityToBuy;
        this.unitPrice = unitPrice;
    }
}
