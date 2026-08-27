package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Document is required")
        String document,
        String phone,

        @NotBlank(message = "Email is required")
        String email,
        String address
) {
}
