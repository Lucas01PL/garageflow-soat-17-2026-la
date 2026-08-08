package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase.CreateClientUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase.DeleteClientUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase.GetClientUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase.ListAllClientsUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase.UpdateClientUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.mapper.ClientMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private CreateClientUseCase createClientUseCase;

    @Mock
    private GetClientUseCase getClientUseCase;

    @Mock
    private UpdateClientUseCase updateUseCase;

    @Mock
    private DeleteClientUseCase deleteClientUseCase;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ListAllClientsUseCase listAllClientsUseCase;

    @InjectMocks
    private ClientController clientController;

    @Test
    void shouldCreateNewClient() {
        ClientRequest request = new ClientRequest("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");
        Client client = new Client("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");
        Client createdClient = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        ClientResponse response = new ClientResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(clientMapper.requestToClient(request)).thenReturn(client);
        when(createClientUseCase.createClient(client)).thenReturn(createdClient);
        when(clientMapper.clientToResponse(createdClient)).thenReturn(response);

        ResponseEntity<ClientResponse> result = clientController.createNewClient(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetClientById() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        ClientResponse response = new ClientResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(getClientUseCase.getClientById("id-1")).thenReturn(Optional.of(client));
        when(clientMapper.clientToResponse(client)).thenReturn(response);

        ResponseEntity<ClientResponse> result = clientController.getClientById("id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetClientByDocument() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        ClientResponse response = new ClientResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(getClientUseCase.getClientByDocument("52998224725")).thenReturn(Optional.of(client));
        when(clientMapper.clientToResponse(client)).thenReturn(response);

        ResponseEntity<ClientResponse> result = clientController.getClientByDocument("52998224725");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAllClients() {
        Client client1 = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        Client client2 = new Client("id-2", "Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");
        ClientResponse response1 = new ClientResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        ClientResponse response2 = new ClientResponse("id-2", "Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");

        when(listAllClientsUseCase.findAll()).thenReturn(List.of(client1, client2));
        when(clientMapper.clientToResponse(client1)).thenReturn(response1);
        when(clientMapper.clientToResponse(client2)).thenReturn(response2);

        ResponseEntity<List<ClientResponse>> result = clientController.getAllClients();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void shouldUpdateClient() {
        ClientRequest request = new ClientRequest("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        Client mappedClient = new Client("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        Client updatedClient = new Client("id-1", "Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        ClientResponse response = new ClientResponse("id-1", "Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");

        when(clientMapper.requestToClient(request)).thenReturn(mappedClient);
        when(updateUseCase.updateClientWithId("id-1", mappedClient)).thenReturn(updatedClient);
        when(clientMapper.clientToResponse(updatedClient)).thenReturn(response);

        ResponseEntity<ClientResponse> result = clientController.updateClient(request, "id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldDeleteClient() {
        ResponseEntity<ClientResponse> result = clientController.deleteClient("id-1");

        assertEquals(204, result.getStatusCode().value());
    }
}
