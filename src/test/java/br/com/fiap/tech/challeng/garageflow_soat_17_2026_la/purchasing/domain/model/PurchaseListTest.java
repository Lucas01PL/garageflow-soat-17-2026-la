package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception.InvalidPurchaseListStatusException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseListTest {

    private PurchaseList pendingPurchaseList() {
        return PurchaseList.builder()
                .id("pl-1")
                .generatedAt(LocalDateTime.now())
                .status(PurchaseListStatus.PENDING)
                .items(List.of(new PurchaseListItem("part-1", "Filtro de oleo", 2, 8, new BigDecimal("29.90"))))
                .build();
    }

    @Test
    void shouldApprovePendingPurchaseList() {
        PurchaseList purchaseList = pendingPurchaseList();

        purchaseList.approve("admin-1");

        assertEquals(PurchaseListStatus.APPROVED, purchaseList.getStatus());
        assertEquals("admin-1", purchaseList.getApprovedBy());
        assertNotNull(purchaseList.getApprovedAt());
    }

    @Test
    void shouldNotApproveNonPendingPurchaseList() {
        PurchaseList purchaseList = pendingPurchaseList();
        purchaseList.approve("admin-1");

        InvalidPurchaseListStatusException ex = assertThrows(
                InvalidPurchaseListStatusException.class,
                () -> purchaseList.approve("admin-2")
        );
        assertTrue(ex.getMessage().contains("Only PENDING purchase lists can be approved"));
    }

    @Test
    void shouldRejectPendingPurchaseList() {
        PurchaseList purchaseList = pendingPurchaseList();

        purchaseList.reject("admin-1");

        assertEquals(PurchaseListStatus.REJECTED, purchaseList.getStatus());
        assertEquals("admin-1", purchaseList.getApprovedBy());
        assertNotNull(purchaseList.getApprovedAt());
    }

    @Test
    void shouldNotRejectNonPendingPurchaseList() {
        PurchaseList purchaseList = pendingPurchaseList();
        purchaseList.reject("admin-1");

        InvalidPurchaseListStatusException ex = assertThrows(
                InvalidPurchaseListStatusException.class,
                () -> purchaseList.reject("admin-2")
        );
        assertTrue(ex.getMessage().contains("Only PENDING purchase lists can be rejected"));
    }

    @Test
    void shouldMarkApprovedPurchaseListAsPurchased() {
        PurchaseList purchaseList = pendingPurchaseList();
        purchaseList.approve("admin-1");

        purchaseList.markAsPurchased();

        assertEquals(PurchaseListStatus.PURCHASED, purchaseList.getStatus());
    }

    @Test
    void shouldNotMarkPendingPurchaseListAsPurchased() {
        PurchaseList purchaseList = pendingPurchaseList();

        InvalidPurchaseListStatusException ex = assertThrows(
                InvalidPurchaseListStatusException.class,
                purchaseList::markAsPurchased
        );
        assertTrue(ex.getMessage().contains("Only APPROVED purchase lists can be purchased"));
    }

    @Test
    void shouldNotMarkRejectedPurchaseListAsPurchased() {
        PurchaseList purchaseList = pendingPurchaseList();
        purchaseList.reject("admin-1");

        assertThrows(InvalidPurchaseListStatusException.class, purchaseList::markAsPurchased);
    }

    @Test
    void shouldDefaultItemsToEmptyListWhenNotSet() {
        PurchaseList purchaseList = PurchaseList.builder()
                .id("pl-2")
                .status(PurchaseListStatus.PENDING)
                .build();

        assertNotNull(purchaseList.getItems());
        assertTrue(purchaseList.getItems().isEmpty());
    }
}
