package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type;

import lombok.Getter;

@Getter
public enum RepairOrderStatus {

    RECEIVED("Recebida"),
    IN_DIAGNOSIS("Em Diagnóstico"),
    AWAITING_APPROVING("Aguardando aprovação"),
    IN_EXECUTION("Em Execução"),
    FINISHED("Finalizada"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelada"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String description;

    RepairOrderStatus(String description) {
        this.description = description;
    }
}
