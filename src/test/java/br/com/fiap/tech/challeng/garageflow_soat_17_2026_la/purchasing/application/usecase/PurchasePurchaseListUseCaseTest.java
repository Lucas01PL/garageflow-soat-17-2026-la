package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception.InvalidPurchaseListStatusException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListItem;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchasePurchaseListUseCaseTest {

    @Mock
    private PurchaseListRepository purchaseListRepository;

    @Mock
    private PartStockControlUseCase partStockControlUseCase;

    @InjectMocks
    private PurchasePurchaseListUseCase useCase;

    @Test
    void shouldMarkAsPurchasedAndRestockEachItem() {
        PurchaseList purchaseList = PurchaseList.builder()
                .id("pl-1")
                .status(PurchaseListStatus.APPROVED)
                .items(List.of(
                        new PurchaseListItem("part-1", "Filtro de oleo", 2, 8, new BigDecimal("29.90")),
                        new PurchaseListItem("part-2", "Pastilha de freio", 0, 10, new BigDecimal("89.90"))
                ))
                .build();

        when(purchaseListRepository.findById("pl-1")).thenReturn(Optional.of(purchaseList));
        when(purchaseListRepository.save(any(PurchaseList.class))).thenAnswer(inv -> inv.getArgument(0));

        PurchaseList result = useCase.execute("pl-1");

        assertEquals(PurchaseListStatus.PURCHASED, result.getStatus());
        verify(partStockControlUseCase).addPartStock("part-1", 8);
        verify(partStockControlUseCase).addPartStock("part-2", 10);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(purchaseListRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute("missing"));
    }

    @Test
    void shouldThrowWhenNotApproved() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        when(purchaseListRepository.findById("pl-1")).thenReturn(Optional.of(purchaseList));

        assertThrows(InvalidPurchaseListStatusException.class, () -> useCase.execute("pl-1"));
    }
}
