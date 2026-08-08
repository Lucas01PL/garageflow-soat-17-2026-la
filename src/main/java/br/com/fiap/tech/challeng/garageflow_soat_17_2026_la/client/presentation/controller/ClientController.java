package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.mapper.ClientMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final UpdateClientUseCase updateUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final ClientMapper clientMapper;
    private final ListAllClientsUseCase listAllClientsUseCase;

    public ClientController(CreateClientUseCase createClientUseCase, GetClientUseCase getClientUseCase, UpdateClientUseCase updateUseCase, DeleteClientUseCase deleteClientUseCase, ClientMapper clientMapper, ListAllClientsUseCase listAllClientsUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.clientMapper = clientMapper;
        this.listAllClientsUseCase = listAllClientsUseCase;
    }

    @Operation(
            summary = "Create Client.",
            description = "Creates a new client."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Client with the given document already exists")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> createNewClient(@Valid @RequestBody ClientRequest request) {
        Client response = createClientUseCase.createClient(clientMapper.requestToClient(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(clientMapper.clientToResponse(response));
    }

    @Operation(
            summary = "Get Client by id.",
            description = "Retrieves a client by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable String id) {
        Optional<Client> clientById = getClientUseCase.getClientById(id);
        ClientResponse clientResponse = clientMapper.clientToResponse(clientById.get());
        return ResponseEntity.status(HttpStatus.OK).body(clientResponse);
    }

    @Operation(
            summary = "Get Client by document.",
            description = "Retrieves a client by its document."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/document/{document}")
    public ResponseEntity<ClientResponse> getClientByDocument(@PathVariable String document) {
        Optional<Client> clientByDocument = getClientUseCase.getClientByDocument(document);
        ClientResponse clientResponse = clientMapper.clientToResponse(clientByDocument.get());
        return ResponseEntity.status(HttpStatus.OK).body(clientResponse);
    }

    @Operation(
            summary = "List Clients.",
            description = "Retrieves all clients."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients listed")
    })
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<Client> allClients = listAllClientsUseCase.findAll();
        List<ClientResponse> clientResponseList = allClients.stream().map(clientMapper::clientToResponse).toList();
        return ResponseEntity.status(HttpStatus.OK).body(clientResponseList);
    }

    @Operation(
            summary = "Update Client.",
            description = "Updates a client by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@Valid @RequestBody ClientRequest request, @PathVariable String id) {
        Client updateClientWithId = updateUseCase.updateClientWithId(id, clientMapper.requestToClient(request));
        return ResponseEntity.status(HttpStatus.OK).body(clientMapper.clientToResponse(updateClientWithId));
    }

    @Operation(
            summary = "Delete Client.",
            description = "Deletes a client by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientResponse> deleteClient(@PathVariable String id) {
        deleteClientUseCase.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
