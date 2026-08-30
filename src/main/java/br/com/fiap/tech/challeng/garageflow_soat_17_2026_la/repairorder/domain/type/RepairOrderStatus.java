package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type;

import lombok.Getter;

import java.util.List;

@Getter
public enum RepairOrderStatus {

    RECEIVED("Recebida"),
    IN_DIAGNOSIS("Em Diagnóstico"),
    AWAITING_APPROVAL("Aguardando aprovação"),
    IN_EXECUTION("Em Execução"),
    FINISHED("Finalizada"),
    DELIVERED("Entregue"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado"),
    CANCELLED("Cancelado");

    private final String description;

    RepairOrderStatus(String description) {
        this.description = description;
    }

    public static List<RepairOrderStatus> preventingPartDeletion() {
        return List.of(
                RECEIVED,
                IN_DIAGNOSIS,
                AWAITING_APPROVAL
        );
    }
}
