package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientMongoRepository clientMongoRepository;

    public ClientRepositoryImpl(ClientMongoRepository clientMongoRepository) {
        this.clientMongoRepository = clientMongoRepository;
    }

    @Override
    public Client save(Client client) {
        ClientDocument savedEntity = clientMongoRepository.save(toEntity(client));
        return toClientDomain(savedEntity);
    }

    @Override
    public Optional<Client> findById(String id) {
        return clientMongoRepository.findById(id).map(this::toClientDomain);
    }

    @Override
    public Optional<Client> findByDocument(String document) {
        return clientMongoRepository.findByDocument(document).map(this::toClientDomain);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientMongoRepository.findByEmail(email).map(this::toClientDomain);
    }

    @Override
    public List<Client> findAll() {
        return clientMongoRepository.findAll()
                .stream()
                .map(this::toClientDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        clientMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByDocument(String document) {
        return clientMongoRepository.existsByDocument(document);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientMongoRepository.existsByEmail(email);
    }

    private Client toClientDomain(ClientDocument clientDocument) {
        return new Client(
                clientDocument.getId(),
                clientDocument.getName(),
                clientDocument.getDocument(),
                clientDocument.getPhone(),
                clientDocument.getEmail(),
                clientDocument.getAddress()
        );
    }

    private ClientDocument toEntity(Client client) {
        ClientDocument document = new ClientDocument(
                client.getName(),
                client.getDocument(),
                client.getPhone(),
                client.getEmail(),
                client.getAddress()
        );
        document.setId(client.getId());
        return document;
    }
}
