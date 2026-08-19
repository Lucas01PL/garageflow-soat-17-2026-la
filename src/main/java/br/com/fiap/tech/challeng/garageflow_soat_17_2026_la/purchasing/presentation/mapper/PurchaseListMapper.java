package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListItem;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto.PurchaseListItemResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto.PurchaseListResponse;
import org.springframework.stereotype.Component;

@Component
public class PurchaseListMapper {

    public PurchaseListResponse toResponse(PurchaseList purchaseList) {
        return new PurchaseListResponse(
                purchaseList.getId(),
                purchaseList.getGeneratedAt(),
                purchaseList.getStatus(),
                purchaseList.getItems().stream().map(this::toItemResponse).toList(),
                purchaseList.getApprovedBy(),
                purchaseList.getApprovedAt()
        );
    }

    private PurchaseListItemResponse toItemResponse(PurchaseListItem item) {
        return new PurchaseListItemResponse(
                item.getPartId(),
                item.getPartName(),
                item.getCurrentQuantity(),
                item.getQuantityToBuy(),
                item.getUnitPrice()
        );
    }
}
