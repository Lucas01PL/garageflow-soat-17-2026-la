package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUserByFullNameUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private SearchUserByFullNameUseCase useCase;

    @Test
    void shouldReturnMatches() {
        User u = new User(); u.setId("1"); u.setFullName("Alice Smith");
        when(repository.findByFullNameContainingIgnoreCase("Alice")).thenReturn(List.of(u));

        var result = useCase.execute("Alice");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice Smith", result.get(0).getFullName());
    }

    @Test
    void shouldThrowWhenFullNameEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(""));
        assertEquals("Full name cannot be empty", ex.getMessage());
    }
}

