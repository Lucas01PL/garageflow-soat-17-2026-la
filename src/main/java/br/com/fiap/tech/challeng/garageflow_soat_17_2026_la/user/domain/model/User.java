package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private String id;
    private String fullName;
    private String email;
    private String password;
    private String status;

    public User() { }

    public User(String fullName, String email, String password, String status) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

