package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserResponseDTO {

    private String id;
    private String fullName;
    private String email;
    private String status;
    private UserRole role;

}

