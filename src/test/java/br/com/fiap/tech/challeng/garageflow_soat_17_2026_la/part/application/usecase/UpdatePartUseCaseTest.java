package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePartUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private UpdatePartUseCase updatePartUseCase;

    @Test
    void shouldUpdateExistingPart() {
        Part existingPart = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        Part updatedData = new Part("P001", "Filtro de oleo premium", 20, new BigDecimal("39.90"));

        when(partRepository.findById("id-1")).thenReturn(Optional.of(existingPart));
        when(partRepository.save(existingPart)).thenReturn(existingPart);

        Part result = updatePartUseCase.updatePartWithId("id-1", updatedData);

        assertEquals("Filtro de oleo premium", result.getName());
        assertEquals(20, result.getQuantity());
        assertEquals(new BigDecimal("39.90"), result.getPrice());

        ArgumentCaptor<Part> captor = ArgumentCaptor.forClass(Part.class);
        verify(partRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenPartDoesNotExist() {
        Part updatedData = new Part("P001", "Filtro de oleo premium", 20, new BigDecimal("39.90"));
        when(partRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updatePartUseCase.updatePartWithId("missing", updatedData));
    }
}
