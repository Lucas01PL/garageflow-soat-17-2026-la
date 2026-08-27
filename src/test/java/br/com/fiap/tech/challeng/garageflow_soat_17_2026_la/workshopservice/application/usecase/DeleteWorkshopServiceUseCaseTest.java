package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredFieldException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteWorkshopServiceUseCaseTest {

    @Mock
    private WorkshopServiceRepository repository;

    @InjectMocks
    private DeleteWorkshopServiceUseCase useCase;

    @Test
    void shouldDeleteWhenExists() {
        when(repository.existsById("1")).thenReturn(true);

        boolean result = useCase.execute("1");

        assertTrue(result);
        verify(repository).deleteById("1");
    }

    @Test
    void shouldReturnFalseWhenNotFound() {
        when(repository.existsById("nope")).thenReturn(false);

        boolean result = useCase.execute("nope");

        assertFalse(result);
    }

    @Test
    void shouldThrowWhenIdIsNullOrBlank() {
        RequiredFieldException ex1 = assertThrows(RequiredFieldException.class, () -> useCase.execute(null));
        assertEquals("Field 'id' is required.", ex1.getMessage());

        RequiredFieldException ex2 = assertThrows(RequiredFieldException.class, () -> useCase.execute("  "));
        assertEquals("Field 'id' is required.", ex2.getMessage());
    }
}

