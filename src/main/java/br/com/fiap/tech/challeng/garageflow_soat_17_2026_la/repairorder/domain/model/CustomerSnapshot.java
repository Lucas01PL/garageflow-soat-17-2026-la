package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CustomerSnapshot {
    private String id;
    private String documentNumber;
    private String fullName;
    private String phone;

    public CustomerSnapshot(String id) {
        this.id = id;
    }
}
