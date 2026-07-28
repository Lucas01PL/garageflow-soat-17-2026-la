package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
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

        assertDoesNotThrow(() -> useCase.execute("1"));

        verify(repository).deleteById("1");
    }

    @Test
    void shouldThrowResourceNotFoundWhenMissing() {
        when(repository.existsById("2")).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> useCase.execute("2"));
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void shouldThrowWhenIdInvalid() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute("") );
        assertEquals("User ID cannot be empty", ex.getMessage());
    }
}

