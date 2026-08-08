package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeleteClientUseCase {

    private final ClientRepository clientRepository;

    public DeleteClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void deleteClient(String id) {
        Optional<Client> byId = clientRepository.findById(id);
        if (byId.isPresent()) {
            log.debug("[DEBUG] - DELETED CLIENT: {}", byId.get());
            clientRepository.delete(id);
            return;
        }
        log.debug("[DEBUG] - Trying to delete non-existant client with id: {}", id);
        throw new ResourceNotFoundException("Client", "id", id);
    }
}
