package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase.LoginUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.LoginRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.LoginResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @InjectMocks
    private AuthController controller;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        when(loginUseCase.execute("admin@garageflow.com", "admin123")).thenReturn("jwt-token");

        ResponseEntity<?> result = controller.login(new LoginRequestDTO("admin@garageflow.com", "admin123"));

        assertEquals(200, result.getStatusCode().value());
        assertInstanceOf(LoginResponseDTO.class, result.getBody());
        assertEquals("jwt-token", ((LoginResponseDTO) result.getBody()).token());
        assertEquals("Bearer", ((LoginResponseDTO) result.getBody()).tokenType());
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() {
        when(loginUseCase.execute("admin@garageflow.com", "wrong-password"))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(AuthenticationException.class, () -> {
            controller.login(new LoginRequestDTO("admin@garageflow.com", "wrong-password"));
        });
    }
}
