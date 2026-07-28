package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private GetUserByIdUseCase useCase;

    @Test
    void shouldReturnUserWhenFound() {
        User u = new User();
        u.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(u));

        User result = useCase.execute("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void shouldThrowWhenIdInvalid() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(""));
        assertEquals("User ID cannot be empty", ex.getMessage());
    }
}

