package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ListAllClientsUseCase {

    private final ClientRepository clientRepository;

    public ListAllClientsUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        log.debug("[DEBUG] - GETTING ALL CLIENTS");
        return clientRepository.findAll();
    }
}
