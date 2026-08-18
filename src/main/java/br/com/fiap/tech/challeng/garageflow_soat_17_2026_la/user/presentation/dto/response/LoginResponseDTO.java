package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response;

public record LoginResponseDTO(
        String token,
        String tokenType
) {
    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}
