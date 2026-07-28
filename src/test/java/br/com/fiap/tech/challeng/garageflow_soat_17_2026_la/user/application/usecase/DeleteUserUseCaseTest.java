package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private DeleteUserUseCase useCase;

    @Test
    void shouldDeleteWhenExists() {
        when(repository.existsById("1")).thenReturn(true);

        boolean result = useCase.execute("1");

        assertTrue(result);
        verify(repository).deleteById("1");
    }

    @Test
    void shouldReturnFalseWhenNotFound() {
        when(repository.existsById("2")).thenReturn(false);
        boolean result = useCase.execute("2");
        assertFalse(result);
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void shouldThrowWhenIdInvalid() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute("") );
        assertEquals("User ID cannot be empty", ex.getMessage());
    }
}

