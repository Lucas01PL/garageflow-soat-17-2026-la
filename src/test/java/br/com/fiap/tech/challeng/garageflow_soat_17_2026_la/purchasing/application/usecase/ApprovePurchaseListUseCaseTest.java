package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception.InvalidPurchaseListStatusException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApprovePurchaseListUseCaseTest {

    @Mock
    private PurchaseListRepository purchaseListRepository;

    @InjectMocks
    private ApprovePurchaseListUseCase useCase;

    @Test
    void shouldApprovePendingPurchaseList() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        when(purchaseListRepository.findById("pl-1")).thenReturn(Optional.of(purchaseList));
        when(purchaseListRepository.save(any(PurchaseList.class))).thenAnswer(inv -> inv.getArgument(0));

        PurchaseList result = useCase.execute("pl-1", "admin-1");

        assertEquals(PurchaseListStatus.APPROVED, result.getStatus());
        assertEquals("admin-1", result.getApprovedBy());
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(purchaseListRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute("missing", "admin-1"));
    }

    @Test
    void shouldThrowWhenAlreadyApproved() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.APPROVED).build();
        when(purchaseListRepository.findById("pl-1")).thenReturn(Optional.of(purchaseList));

        assertThrows(InvalidPurchaseListStatusException.class, () -> useCase.execute("pl-1", "admin-1"));
    }
}
