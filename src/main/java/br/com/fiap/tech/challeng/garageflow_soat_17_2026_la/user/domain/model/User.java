package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@Data
public class User {

    @Id
    private String id;
    private String fullName;
    private String email;
    private String password;
    private String status;
    private UserRole role;

    public User(String fullName, String email, String password, String status, UserRole role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
    }

}