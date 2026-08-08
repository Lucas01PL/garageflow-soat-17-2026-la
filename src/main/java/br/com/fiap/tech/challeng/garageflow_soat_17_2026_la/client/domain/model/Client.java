package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Client {

    private String id;
    private String name;
    private String document;
    private String phone;
    private String email;

    public Client(String name, String document, String phone, String email) {
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
    }

    public Client(String id, String name, String document, String phone, String email) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
    }

    public void update(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
