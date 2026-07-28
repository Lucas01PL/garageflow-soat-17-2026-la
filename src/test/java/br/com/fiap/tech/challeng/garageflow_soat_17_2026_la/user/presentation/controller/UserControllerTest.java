package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.UserResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.CreateUserRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateUserUseCase createUseCase;

    @Mock
    private GetUserByIdUseCase getByIdUseCase;

    @Mock
    private UpdateUserUseCase updateUseCase;

    @Mock
    private DeleteUserUseCase deleteUseCase;

    @Mock
    private ListAllUsersUseCase listAllUseCase;

    @Mock
    private SearchUserByFullNameUseCase searchUseCase;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserController controller;

    private CreateUserRequestDTO createDto;
    private User model;
    private UserResponseDTO responseDto;

    @BeforeEach
    void setup() {
        createDto = new CreateUserRequestDTO("Name", "a@b.com", "secret", "ACTIVE");
        model = new User();
        model.setId("1");
        model.setFullName("Name");
        model.setEmail("a@b.com");
        model.setStatus("ACTIVE");

        responseDto = new UserResponseDTO("1", "Name", "a@b.com", "ACTIVE");
    }

    @Test
    void createShouldReturnCreated() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(createUseCase.execute(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.create(createDto);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        Object body = resp.getBody();
        assertTrue(body instanceof UserResponseDTO);
        UserResponseDTO bodyDto = (UserResponseDTO) body;
        assertEquals(responseDto.getId(), bodyDto.getId());
        assertEquals(responseDto.getFullName(), bodyDto.getFullName());
        assertEquals(responseDto.getEmail(), bodyDto.getEmail());
        assertEquals(responseDto.getStatus(), bodyDto.getStatus());
    }

    @Test
    void createShouldReturnBadRequestWhenUseCaseThrows() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(createUseCase.execute(model)).thenThrow(new IllegalArgumentException("bad"));

        ResponseEntity<?> resp = controller.create(createDto);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("bad", resp.getBody());
    }

    @Test
    void getByIdShouldReturnOkWhenFound() {
        when(getByIdUseCase.execute("1")).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.getById("1");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Object body2 = resp.getBody();
        assertTrue(body2 instanceof UserResponseDTO);
        UserResponseDTO bodyDto2 = (UserResponseDTO) body2;
        assertEquals(responseDto.getId(), bodyDto2.getId());
        assertEquals(responseDto.getFullName(), bodyDto2.getFullName());
        assertEquals(responseDto.getEmail(), bodyDto2.getEmail());
        assertEquals(responseDto.getStatus(), bodyDto2.getStatus());
    }

    @Test
    void getByIdShouldReturnNotFoundWhenMissing() {
        when(getByIdUseCase.execute("x")).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getById("x");

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNull(resp.getBody());
    }

    @Test
    void listAllShouldReturnList() {
        when(listAllUseCase.execute()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<List<UserResponseDTO>> resp = controller.listAll();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void searchShouldReturnResults() {
        when(searchUseCase.execute("Name")).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.search("Name");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void updateShouldReturnOkWhenUpdated() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(updateUseCase.execute("1", model)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.update("1", createDto);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void updateShouldReturnNotFoundWhenMissing() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(updateUseCase.execute("x", model)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.update("x", createDto);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void deleteShouldReturnNoContentWhenDeleted() {
        when(deleteUseCase.execute("1")).thenReturn(true);

        ResponseEntity<?> resp = controller.delete("1");

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    void deleteShouldReturnNotFoundWhenMissing() {
        when(deleteUseCase.execute("x")).thenReturn(false);

        ResponseEntity<?> resp = controller.delete("x");

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals("User not found", resp.getBody());
    }

}


