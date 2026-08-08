package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.presentation.dto.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client requestToClient(ClientRequest clientRequest) {
        return new Client(
                clientRequest.name(),
                clientRequest.document(),
                clientRequest.phone(),
                clientRequest.email(),
                clientRequest.address()
        );
    }

    public ClientResponse clientToResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getDocument(),
                client.getPhone(),
                client.getEmail(),
                client.getAddress()
        );
    }
}
