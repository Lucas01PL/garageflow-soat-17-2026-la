package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartListToBuyUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratePurchaseListUseCaseTest {

    @Mock
    private PartListToBuyUseCase partListToBuyUseCase;

    @Mock
    private PurchaseListRepository purchaseListRepository;

    @InjectMocks
    private GeneratePurchaseListUseCase useCase;

    @Test
    void shouldGeneratePurchaseListFromLowStockParts() {
        Part part1 = new Part("part-1", "P001", "Filtro de oleo", 2, new BigDecimal("29.90"));
        Part part2 = new Part("part-2", "P002", "Pastilha de freio", 0, new BigDecimal("89.90"));

        when(partListToBuyUseCase.findPartsToBuy(5)).thenReturn(List.of(part1, part2));
        when(purchaseListRepository.save(any(PurchaseList.class))).thenAnswer(inv -> inv.getArgument(0));

        PurchaseList result = useCase.generate(5);

        assertEquals(PurchaseListStatus.PENDING, result.getStatus());
        assertEquals(2, result.getItems().size());
        assertEquals("part-1", result.getItems().get(0).getPartId());
        assertEquals(8, result.getItems().get(0).getQuantityToBuy());
        assertEquals(10, result.getItems().get(1).getQuantityToBuy());
    }

    @Test
    void shouldApplyDefaultThresholdWhenNoneProvided() {
        when(partListToBuyUseCase.findPartsToBuy(PartListToBuyUseCase.DEFAULT_LOW_STOCK_THRESHOLD)).thenReturn(List.of());
        when(purchaseListRepository.save(any(PurchaseList.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.generate(null);

        verify(partListToBuyUseCase).findPartsToBuy(PartListToBuyUseCase.DEFAULT_LOW_STOCK_THRESHOLD);
    }

    @Test
    void shouldNeverBuyLessThanOneUnit() {
        Part part = new Part("part-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partListToBuyUseCase.findPartsToBuy(5)).thenReturn(List.of(part));

        ArgumentCaptor<PurchaseList> captor = ArgumentCaptor.forClass(PurchaseList.class);
        when(purchaseListRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        useCase.generate(5);

        assertEquals(1, captor.getValue().getItems().get(0).getQuantityToBuy());
    }
}
