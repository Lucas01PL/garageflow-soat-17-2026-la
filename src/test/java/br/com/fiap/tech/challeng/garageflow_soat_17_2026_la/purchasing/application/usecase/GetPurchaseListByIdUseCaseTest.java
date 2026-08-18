package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPurchaseListByIdUseCaseTest {

    @Mock
    private PurchaseListRepository purchaseListRepository;

    @InjectMocks
    private GetPurchaseListByIdUseCase useCase;

    @Test
    void shouldReturnPurchaseListWhenFound() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        when(purchaseListRepository.findById("pl-1")).thenReturn(Optional.of(purchaseList));

        PurchaseList result = useCase.execute("pl-1");

        assertEquals("pl-1", result.getId());
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(purchaseListRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute("missing"));
    }
}
