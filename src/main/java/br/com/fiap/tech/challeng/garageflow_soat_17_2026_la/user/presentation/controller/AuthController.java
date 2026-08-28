package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase.LoginUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.LoginRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user with email and password and returns a JWT token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = loginUseCase.execute(dto.email(), dto.password());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
