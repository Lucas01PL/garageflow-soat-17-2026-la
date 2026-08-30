package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidFieldValueException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.RequiredObjectException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void shouldCreateUserSuccessfully() {
        User toCreate = new User();
        toCreate.setFullName("John Doe");
        toCreate.setEmail("john.doe@example.com");
        toCreate.setPassword("secret123");
        toCreate.setStatus("ACTIVE");
        toCreate.setRole(UserRole.CUSTOMER);

        User saved = new User();
        saved.setId("1");
        saved.setFullName(toCreate.getFullName());
        saved.setEmail(toCreate.getEmail());
        saved.setPassword("encoded-password");
        saved.setStatus(toCreate.getStatus());

        when(repository.findByEmail(toCreate.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenReturn(saved);

        User result = useCase.execute(toCreate);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void shouldThrowWhenUserIsNull() {
        RequiredObjectException ex = assertThrows(RequiredObjectException.class, () -> useCase.execute(null));
        assertEquals("User cannot be null cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        User toCreate = new User();
        toCreate.setFullName("Jane");
        toCreate.setEmail("jane@example.com");
        toCreate.setPassword("password");
        toCreate.setStatus("ACTIVE");
        toCreate.setRole(UserRole.CUSTOMER);

        when(repository.findByEmail(toCreate.getEmail())).thenReturn(Optional.of(new User()));

        InvalidFieldValueException ex = assertThrows(InvalidFieldValueException.class, () -> useCase.execute(toCreate));
        assertEquals("Field 'email' is invalid. Email already exists", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordTooShort() {
        User toCreate = new User();
        toCreate.setFullName("Jane");
        toCreate.setEmail("jane2@example.com");
        toCreate.setPassword("123");
        toCreate.setStatus("ACTIVE");

        InvalidFieldValueException ex = assertThrows(InvalidFieldValueException.class, () -> useCase.execute(toCreate));
        assertEquals("Field 'password' is invalid. Password must be at least 6 characters", ex.getMessage());
    }
}

