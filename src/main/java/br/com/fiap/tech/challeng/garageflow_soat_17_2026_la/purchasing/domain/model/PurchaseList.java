package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception.InvalidPurchaseListStatusException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PurchaseList {

    private String id;
    private LocalDateTime generatedAt;
    private PurchaseListStatus status;

    @Builder.Default
    private List<PurchaseListItem> items = new ArrayList<>();

    private String approvedBy;
    private LocalDateTime approvedAt;

    public void approve(String approvedBy) {
        if (status != PurchaseListStatus.PENDING) {
            throw new InvalidPurchaseListStatusException(
                    "Only PENDING purchase lists can be approved. Current status: " + status);
        }
        this.status = PurchaseListStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }

    public void reject(String rejectedBy) {
        if (status != PurchaseListStatus.PENDING) {
            throw new InvalidPurchaseListStatusException(
                    "Only PENDING purchase lists can be rejected. Current status: " + status);
        }
        this.status = PurchaseListStatus.REJECTED;
        this.approvedBy = rejectedBy;
        this.approvedAt = LocalDateTime.now();
    }

    public void markAsPurchased() {
        if (status != PurchaseListStatus.APPROVED) {
            throw new InvalidPurchaseListStatusException(
                    "Only APPROVED purchase lists can be purchased. Current status: " + status);
        }
        this.status = PurchaseListStatus.PURCHASED;
    }
}
