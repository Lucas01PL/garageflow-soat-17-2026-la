package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.configuration.security.JwtService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginUseCase useCase;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        User user = new User();
        user.setEmail("admin@garageflow.com");
        user.setRole(UserRole.ADMIN);

        when(userRepository.findByEmail("admin@garageflow.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("admin@garageflow.com", "ADMIN")).thenReturn("jwt-token");

        String token = useCase.execute("admin@garageflow.com", "admin123");

        assertEquals("jwt-token", token);
    }

    @Test
    void shouldPropagateAuthenticationException() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> useCase.execute("admin@garageflow.com", "wrong-password"));
    }

    @Test
    void shouldThrowWhenAuthenticatedUserNoLongerExists() {
        when(userRepository.findByEmail("ghost@garageflow.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute("ghost@garageflow.com", "password123"));
    }
}
