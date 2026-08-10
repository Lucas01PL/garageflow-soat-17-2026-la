package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.validation.CpfCnpjValidator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class GetClientUseCase {

    private final ClientRepository clientRepository;

    public GetClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<Client> getClientByDocument(String document) {
        String normalizedDocument = CpfCnpjValidator.validateAndNormalize(document);

        Optional<Client> byDocument = clientRepository.findByDocument(normalizedDocument);
        if (byDocument.isPresent()) {
            log.debug("[DEBUG] - GET CLIENT: {}", byDocument);
            return byDocument;
        }
        throw new ResourceNotFoundException("Client", "document", normalizedDocument);
    }

    public Optional<Client> getClientById(String id) {
        Optional<Client> byId = clientRepository.findById(id);
        if (byId.isPresent()) {
            return byId;
        }
        throw new ResourceNotFoundException("Client", "id", id);
    }
}
