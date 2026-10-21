package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type;

import lombok.Getter;

import java.util.List;

@Getter
public enum RepairOrderStatus {

    RECEIVED(1, "Recebida"),
    IN_DIAGNOSIS(2, "Diagnóstico"),
    AWAITING_APPROVAL(3, "Aguardando aprovação"),
    IN_EXECUTION(4, "Em Execução"),
    FINISHED(5, "Finalizada"),
    DELIVERED(6, "Entregue"),
    APPROVED(7, "Aprovado"),
    REJECTED(8, "Rejeitado"),
    CANCELLED(9, "Cancelado");

    private final Integer code;
    private final String description;

    RepairOrderStatus(Integer code, String description) {
        this.code = code;
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
