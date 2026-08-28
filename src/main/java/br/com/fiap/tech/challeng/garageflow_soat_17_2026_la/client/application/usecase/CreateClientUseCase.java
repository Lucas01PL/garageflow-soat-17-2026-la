package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.validation.CpfCnpjValidator;
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
        String normalizedDocument = CpfCnpjValidator.validateAndNormalize(client.getDocument());

        boolean isPresentWithDocument = clientRepository.existsByDocument(normalizedDocument);
        boolean isPresentWithEmail = clientRepository.existsByEmail(client.getEmail());

        if (isPresentWithEmail) {
            log.debug("[DEBUG] - Trying to register duplicated client with e-mail: {}", client.getEmail());
            throw new DuplicateResourceException("Client", "e-mail", client.getEmail());
        }

        if (isPresentWithDocument) {
            log.debug("[DEBUG] - Trying to register duplicated client with document: {}", normalizedDocument);
            throw new DuplicateResourceException("Client", "document", normalizedDocument);
        }

        Client normalizedClient = new Client(
                client.getName(),
                normalizedDocument,
                client.getPhone(),
                client.getEmail(),
                client.getAddress()
        );

        log.debug("[DEBUG] - POST CLIENT: {}", normalizedClient);
        return clientRepository.save(normalizedClient);
    }
}
