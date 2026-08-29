package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.infrastructure.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Getter
@Setter
@NoArgsConstructor
public class CustomerDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String document;

    private String name;
    private String phone;

    @Indexed(unique = true)
    private String email;
    private String address;

    public CustomerDocument(String name, String document, String phone, String email, String address) {
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
}
