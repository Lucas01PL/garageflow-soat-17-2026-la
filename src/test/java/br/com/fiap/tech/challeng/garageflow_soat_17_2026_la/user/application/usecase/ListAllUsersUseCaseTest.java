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
class ListAllUsersUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private ListAllUsersUseCase useCase;

    @Test
    void shouldReturnListOfUsers() {
        User a = new User(); a.setId("1");
        User b = new User(); b.setId("2");

        when(repository.findAll()).thenReturn(List.of(a, b));

        var result = useCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}

