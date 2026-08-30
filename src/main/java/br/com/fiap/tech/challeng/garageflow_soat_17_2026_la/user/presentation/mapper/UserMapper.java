package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.CreateUserRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toModel(CreateUserRequestDTO dto) {
        if (dto == null) return null;
        return new User(dto.getFullName(), dto.getEmail(), dto.getPassword(), dto.getStatus(), dto.getRole());
    }

    public UserResponseDTO toResponse(User model) {
        if (model == null) return null;
        return new UserResponseDTO(
                model.getId(),
                model.getFullName(),
                model.getEmail(),
                model.getStatus(),
                model.getRole()
        );
    }
}

