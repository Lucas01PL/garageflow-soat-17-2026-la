package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.infrastructure.persistence.document;

import java.time.Instant;

public record RepairOrderApprovalLinkDocument(
        String tokenHash,
        Instant createdAt,
        Instant expiresAt,
        Instant usedAt
) {
}