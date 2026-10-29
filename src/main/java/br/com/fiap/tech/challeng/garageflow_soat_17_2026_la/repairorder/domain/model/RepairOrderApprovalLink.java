package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la
        .repairorder.domain.model;

import java.time.Instant;

public record RepairOrderApprovalLink(
        String tokenHash,
        Instant createdAt,
        Instant expiresAt,
        Instant usedAt
) {
    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    public boolean isValid(Instant now) {
        return !isUsed() && !isExpired(now);
    }

    public RepairOrderApprovalLink markAsUsed(Instant now) {
        return new RepairOrderApprovalLink(
                tokenHash,
                createdAt,
                expiresAt,
                now
        );
    }
}