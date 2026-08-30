package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import java.time.Instant;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        String path
) {
}