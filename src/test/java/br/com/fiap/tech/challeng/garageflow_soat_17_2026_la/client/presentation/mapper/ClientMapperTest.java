package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClientMapperTest {

    private final ClientMapper clientMapper = new ClientMapper();

    @Test
    void shouldMapRequestToClient() {
        ClientRequest request = new ClientRequest("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        Client client = clientMapper.requestToClient(request);

        assertNull(client.getId());
        assertEquals("Joao Silva", client.getName());
        assertEquals("52998224725", client.getDocument());
        assertEquals("11999998888", client.getPhone());
        assertEquals("joao@email.com", client.getEmail());
        assertEquals("Rua A, 123", client.getAddress());
    }

    @Test
    void shouldMapClientToResponse() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        ClientResponse response = clientMapper.clientToResponse(client);

        assertEquals("id-1", response.id());
        assertEquals("Joao Silva", response.name());
        assertEquals("52998224725", response.document());
        assertEquals("11999998888", response.phone());
        assertEquals("joao@email.com", response.email());
        assertEquals("Rua A, 123", response.address());
    }
}
