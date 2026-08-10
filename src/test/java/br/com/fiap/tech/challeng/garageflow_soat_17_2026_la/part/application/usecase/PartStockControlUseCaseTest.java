package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.NotEnoughResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartStockControlUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartStockControlUseCase partStockControlUseCase;

    @Test
    void shouldDebitStockWhenThereIsEnoughQuantity() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        String result = partStockControlUseCase.debitPartStock("id-1", 4);

        assertTrue(result.contains("Debited successfully"));
        ArgumentCaptor<Part> captor = ArgumentCaptor.forClass(Part.class);
        verify(partRepository).save(captor.capture());
        assertEquals(6, captor.getValue().getQuantity());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDebitingNonExistentPart() {
        when(partRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> partStockControlUseCase.debitPartStock("missing", 1));
        verify(partRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowNotEnoughResourceExceptionWhenCurrentQuantityIsZero() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 0, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        assertThrows(NotEnoughResourceException.class, () -> partStockControlUseCase.debitPartStock("id-1", 1));
        verify(partRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowNotEnoughResourceExceptionWhenQuantityToDebitExceedsStock() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 3, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        assertThrows(NotEnoughResourceException.class, () -> partStockControlUseCase.debitPartStock("id-1", 5));
        verify(partRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldAddStockWhenQuantityIsGreaterThanZero() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        String result = partStockControlUseCase.addPartStock("id-1", 5);

        assertTrue(result.contains("Added successfully"));
        ArgumentCaptor<Part> captor = ArgumentCaptor.forClass(Part.class);
        verify(partRepository).save(captor.capture());
        assertEquals(15, captor.getValue().getQuantity());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAddingStockToNonExistentPart() {
        when(partRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> partStockControlUseCase.addPartStock("missing", 1));
        verify(partRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowNotEnoughResourceExceptionWhenQuantityToAddIsNotGreaterThanZero() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        assertThrows(NotEnoughResourceException.class, () -> partStockControlUseCase.addPartStock("id-1", 0));
        verify(partRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
