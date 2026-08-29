package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Document is required")
        String document,

        @NotBlank(message = "Phone is required")
        String phone,

        @Email(message = "Email is invalid")
        String email,
        String address
) {
}
