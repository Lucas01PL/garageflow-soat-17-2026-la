package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.type;

import lombok.Getter;

@Getter
public enum WorkshopServiceStatus {
    WAITING_ATTENDING("Aguardando atendimento"),
    IN_EXECUTION("Em Execução"),
    FINISHED("Finalizado");

    private final String description;

    WorkshopServiceStatus(String description) {
        this.description = description;
    }
}
