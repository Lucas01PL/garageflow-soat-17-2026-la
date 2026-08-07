package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.DuplicatePartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePartUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private CreatePartUseCase createPartUseCase;

    @Test
    void shouldCreatePartWhenCodeIsNotDuplicated() {
        Part part = new Part("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        Part saved = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        when(partRepository.existsByCode("P001")).thenReturn(false);
        when(partRepository.save(part)).thenReturn(saved);

        Part result = createPartUseCase.createPart(part);

        assertNotNull(result);
        assertEquals("id-1", result.getId());
        assertEquals("P001", result.getCode());
        verify(partRepository).save(part);
    }

    @Test
    void shouldThrowDuplicatePartExceptionWhenCodeAlreadyExists() {
        Part part = new Part("P001", "Filtro de oleo", 10, new BigDecimal("29.90"));

        when(partRepository.existsByCode("P001")).thenReturn(true);

        assertThrows(DuplicatePartException.class, () -> createPartUseCase.createPart(part));
        verify(partRepository, never()).save(any());
    }
}
