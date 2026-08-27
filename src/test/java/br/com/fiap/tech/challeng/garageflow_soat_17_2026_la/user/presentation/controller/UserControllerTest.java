package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.UserRole;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.UserResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.CreateUserRequestDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

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
        createDto = new CreateUserRequestDTO("Name", "a@b.com", "secret", "ACTIVE", UserRole.CUSTOMER);
        model = new User();
        model.setId("1");
        model.setFullName("Name");
        model.setEmail("a@b.com");
        model.setStatus("ACTIVE");
        model.setRole(UserRole.CUSTOMER);

        responseDto = new UserResponseDTO("1", "Name", "a@b.com", "ACTIVE", UserRole.CUSTOMER);
    }

    @Test
    void createShouldReturnCreated() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(createUseCase.execute(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.create(createDto);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertInstanceOf(UserResponseDTO.class, resp.getBody());
    }

    @Test
    void getByIdShouldReturnOkWhenFound() {
        when(getByIdUseCase.execute("1")).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.getById("1");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertInstanceOf(UserResponseDTO.class, resp.getBody());
    }

    @Test
    void getByIdShouldThrowResourceNotFoundWhenMissing() {
        when(getByIdUseCase.execute("x")).thenThrow(new ResourceNotFoundException("User", "id", "x"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getById("x"));
    }

    @Test
    void listAllShouldReturnList() {
        when(listAllUseCase.execute()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<List<UserResponseDTO>> resp = controller.list("");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void searchShouldReturnResults() {
        when(searchUseCase.execute("Name")).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.list("Name");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void updateShouldReturnOkWhenUpdated() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(updateUseCase.execute("1", model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(responseDto);

        ResponseEntity<?> resp = controller.update("1", createDto);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void updateShouldThrowResourceNotFoundWhenMissing() {
        when(mapper.toModel(createDto)).thenReturn(model);
        when(updateUseCase.execute("x", model)).thenThrow(new ResourceNotFoundException("User", "id", "x"));

        assertThrows(ResourceNotFoundException.class, () -> controller.update("x", createDto));
    }

    @Test
    void deleteShouldReturnNoContentWhenDeleted() {
//        doThrow(new ResourceNotFoundException("User", "id", "1")).when(deleteUseCase).execute("1");

        ResponseEntity<?> resp = controller.delete("1");

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    void deleteShouldThrowResourceNotFoundWhenMissing() {
        doThrow(new ResourceNotFoundException("User", "id", "x")).when(deleteUseCase).execute("x");

        assertThrows(ResourceNotFoundException.class, () -> controller.delete("x"));
    }

}


