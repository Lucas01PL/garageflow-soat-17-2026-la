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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UpdateUserUseCase useCase;

    @Test
    void shouldUpdateUserSuccessfully() {
        User existing = new User();
        existing.setId("1");
        existing.setFullName("Old Name");
        existing.setEmail("old@example.com");
        existing.setPassword("oldpass");
        existing.setStatus("ACTIVE");

        User update = new User();
        update.setFullName("New Name");
        update.setEmail("new@example.com");
        update.setPassword("newpassword");
        update.setStatus("INACTIVE");

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<User> result = useCase.execute("1", update);

        assertTrue(result.isPresent());
        User u = result.get();
        assertEquals("New Name", u.getFullName());
        assertEquals("new@example.com", u.getEmail());
        assertEquals("newpassword", u.getPassword());
        assertEquals("INACTIVE", u.getStatus());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        when(repository.findById("not-exist")).thenReturn(Optional.empty());
        Optional<User> result = useCase.execute("not-exist", new User());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, new User()));
        assertEquals("User ID cannot be empty", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailInvalid() {
        User existing = new User();
        existing.setId("2");
        when(repository.findById("2")).thenReturn(Optional.of(existing));

        User update = new User();
        update.setEmail("invalid-email");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute("2", update));
        assertEquals("Email format is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordTooShort() {
        User existing = new User();
        existing.setId("3");
        when(repository.findById("3")).thenReturn(Optional.of(existing));

        User update = new User();
        update.setPassword("123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute("3", update));
        assertEquals("Password must be at least 6 characters", ex.getMessage());
    }
}

