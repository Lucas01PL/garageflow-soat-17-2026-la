package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.DuplicateResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateClientUseCase {

    private final ClientRepository clientRepository;

    public CreateClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        boolean isPresent = clientRepository.existsByDocument(client.getDocument());
        if (isPresent) {
            log.debug("[DEBUG] - Trying to register duplicated client with document: {}", client.getDocument());
            throw new DuplicateResourceException("Client", "document", client.getDocument());
        }

        log.debug("[DEBUG] - POST CLIENT: {}", client);
        return clientRepository.save(client);
    }
}
