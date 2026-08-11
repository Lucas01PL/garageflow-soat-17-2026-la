package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Data
public class CustomerSnapshot {
    private String customerId;
    private String name;
    private String document;
    private String phone;
    private String email;
    private String address;

    public CustomerSnapshot(String customerId) {
        this.customerId = customerId;
    }

    public static CustomerSnapshot from(Client customer) {
        Objects.requireNonNull(customer);

        CustomerSnapshot snapshot = new CustomerSnapshot(customer.getId());
        snapshot.setName(customer.getName());
        snapshot.setDocument(customer.getDocument());
        snapshot.setPhone(customer.getPhone());
        snapshot.setEmail(customer.getEmail());
        snapshot.setAddress(customer.getAddress());
        return snapshot;
    }
}
