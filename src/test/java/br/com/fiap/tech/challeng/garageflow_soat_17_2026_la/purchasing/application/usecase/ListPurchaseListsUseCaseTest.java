package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.repository.PurchaseListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPurchaseListsUseCaseTest {

    @Mock
    private PurchaseListRepository purchaseListRepository;

    @InjectMocks
    private ListPurchaseListsUseCase useCase;

    @Test
    void shouldListAllWhenStatusIsNull() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        when(purchaseListRepository.findAll()).thenReturn(List.of(purchaseList));

        List<PurchaseList> result = useCase.execute(null);

        assertEquals(1, result.size());
        verify(purchaseListRepository, never()).findByStatus(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldFilterByStatusWhenProvided() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.APPROVED).build();
        when(purchaseListRepository.findByStatus(PurchaseListStatus.APPROVED)).thenReturn(List.of(purchaseList));

        List<PurchaseList> result = useCase.execute(PurchaseListStatus.APPROVED);

        assertEquals(1, result.size());
        verify(purchaseListRepository).findByStatus(PurchaseListStatus.APPROVED);
    }
}
