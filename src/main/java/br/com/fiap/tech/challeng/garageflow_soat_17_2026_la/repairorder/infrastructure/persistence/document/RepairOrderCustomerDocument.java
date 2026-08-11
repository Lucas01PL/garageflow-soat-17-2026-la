package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.infrastructure.persistence.document;

import lombok.Data;

@Data
public class RepairOrderCustomerDocument {

    private String customerId;
    private String name;
    private String document;
    private String phone;
    private String email;
    private String address;
}
