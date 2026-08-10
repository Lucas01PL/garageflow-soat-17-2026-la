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
    private String address;

    public Client(String name, String document, String phone, String email, String address) {
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Client(String id, String name, String document, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public void update(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
}
