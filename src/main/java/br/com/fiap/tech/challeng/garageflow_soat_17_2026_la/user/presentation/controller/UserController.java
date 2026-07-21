package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.domain.model.User;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.response.CreateUserRequestDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.dto.request.UserResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.user.presentation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CreateUserUseCase createUseCase;

    @Autowired
    private GetUserByIdUseCase getByIdUseCase;

    @Autowired
    private UpdateUserUseCase updateUseCase;

    @Autowired
    private DeleteUserUseCase deleteUseCase;

    @Autowired
    private ListAllUsersUseCase listAllUseCase;

    @Autowired
    private SearchUserByFullNameUseCase searchUseCase;

    @Autowired
    private UserMapper mapper;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequestDTO dto) {
        try {
            User user = mapper.toModel(dto);
            User created = createUseCase.execute(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<User> user = getByIdUseCase.execute(id);
            return user.map(u -> ResponseEntity.ok(mapper.toResponse(u)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll() {
        List<User> list = listAllUseCase.execute();
        List<UserResponseDTO> dtos = list.stream().map(mapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody CreateUserRequestDTO dto) {
        try {
            User update = mapper.toModel(dto);
            Optional<User> updated = updateUseCase.execute(id, update);
            return updated.map(u -> ResponseEntity.ok(mapper.toResponse(u)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean deleted = deleteUseCase.execute(id);
            if (deleted) return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

