package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.UserResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.CreateUserRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toModelShouldMapDtoToModel() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO("Full Name", "e@mail.com", "secret", "ACTIVE");

        User model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals("Full Name", model.getFullName());
        assertEquals("e@mail.com", model.getEmail());
        assertEquals("secret", model.getPassword());
        assertEquals("ACTIVE", model.getStatus());
    }

    @Test
    void toModelShouldReturnNullWhenDtoIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toResponseShouldMapModelToDto() {
        User model = new User();
        model.setId("10");
        model.setFullName("F");
        model.setEmail("a@b.com");
        model.setStatus("ACTIVE");

        UserResponseDTO dto = mapper.toResponse(model);

        assertNotNull(dto);
        assertEquals("10", dto.getId());
        assertEquals("F", dto.getFullName());
        assertEquals("a@b.com", dto.getEmail());
        assertEquals("ACTIVE", dto.getStatus());
    }

    @Test
    void toResponseShouldReturnNullWhenModelIsNull() {
        assertNull(mapper.toResponse(null));
    }
}

