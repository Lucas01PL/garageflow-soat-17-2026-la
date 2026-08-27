package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.CreateUserRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.UserResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private CreateUserUseCase createUseCase;

    private GetUserByIdUseCase getByIdUseCase;

    private UpdateUserUseCase updateUseCase;

    private DeleteUserUseCase deleteUseCase;

    private ListAllUsersUseCase listAllUseCase;

    private SearchUserByFullNameUseCase searchUseCase;

    private UserMapper mapper;

    @Operation(
            summary = "Create User",
            description = "Creates a new user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequestDTO dto) {
        User user = mapper.toModel(dto);
        User created = createUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @Operation(
            summary = "Get User by ID",
            description = "Retrieves a user by their ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        User user = getByIdUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(user));
    }

    @Operation(
            summary = "List All Users",
            description = "Retrieves a list of all users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll() {
        List<User> list = listAllUseCase.execute();
        List<UserResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Search Users by Full Name",
            description = "Searches for users by their full name."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String fullName) {
        try {
            List<User> list = searchUseCase.execute(fullName);
            List<UserResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Update User",
            description = "Updates a user by their ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody CreateUserRequestDTO dto) {
        try {
            User update = mapper.toModel(dto);
            User updated = updateUseCase.execute(id, update);
            return ResponseEntity.ok(mapper.toResponse(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete User",
            description = "Deletes a user by their ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

