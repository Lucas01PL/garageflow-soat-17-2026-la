package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletePartUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private DeletePartUseCase deletePartUseCase;

    @Test
    void shouldDeletePartWhenExists() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        deletePartUseCase.deletePart("id-1");

        verify(partRepository).delete("id-1");
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenPartDoesNotExist() {
        when(partRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deletePartUseCase.deletePart("missing"));
        verify(partRepository, never()).delete(anyString());
    }
}
