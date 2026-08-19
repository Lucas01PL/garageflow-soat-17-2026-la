package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartListToBuyUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartListToBuyUseCase useCase;

    @Test
    void shouldUseProvidedThreshold() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 2, new BigDecimal("29.90"));
        when(partRepository.findLowStock(10)).thenReturn(List.of(part));

        List<Part> result = useCase.findPartsToBuy(10);

        assertEquals(1, result.size());
        assertEquals("id-1", result.get(0).getId());
    }

    @Test
    void shouldUseDefaultThresholdWhenNullProvided() {
        when(partRepository.findLowStock(PartListToBuyUseCase.DEFAULT_LOW_STOCK_THRESHOLD)).thenReturn(List.of());

        List<Part> result = useCase.findPartsToBuy(null);

        assertEquals(0, result.size());
    }
}
