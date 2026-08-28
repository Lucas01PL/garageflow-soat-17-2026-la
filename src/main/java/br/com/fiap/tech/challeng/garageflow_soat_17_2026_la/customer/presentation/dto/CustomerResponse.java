package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto;

public record CustomerResponse(
        String id,
        String name,
        String document,
        String phone,
        String email,
        String address
) {
}
