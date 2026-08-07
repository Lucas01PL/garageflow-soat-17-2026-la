package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.model.Part;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.repository.PartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPartUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private GetPartUseCase getPartUseCase;

    @Test
    void shouldReturnPartWhenCodeExists() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partRepository.findByCode("P001")).thenReturn(Optional.of(part));

        Optional<Part> result = getPartUseCase.getPartbyCode("P001");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldThrowPartNotFoundExceptionWhenCodeDoesNotExist() {
        when(partRepository.findByCode("missing")).thenReturn(Optional.empty());

        assertThrows(PartNotFoundException.class, () -> getPartUseCase.getPartbyCode("missing"));
    }

    @Test
    void shouldReturnPartWhenIdExists() {
        Part part = new Part("id-1", "P001", "Filtro de oleo", 10, new BigDecimal("29.90"));
        when(partRepository.findById("id-1")).thenReturn(Optional.of(part));

        Optional<Part> result = getPartUseCase.getPartbyId("id-1");

        assertTrue(result.isPresent());
        assertEquals("P001", result.get().getCode());
    }

    @Test
    void shouldThrowPartNotFoundExceptionWhenIdDoesNotExist() {
        when(partRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(PartNotFoundException.class, () -> getPartUseCase.getPartbyId("missing"));
    }
}
