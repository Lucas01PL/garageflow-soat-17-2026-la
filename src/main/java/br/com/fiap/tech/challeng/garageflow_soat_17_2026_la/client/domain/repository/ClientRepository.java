package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    Client save(Client client);

    Optional<Client> findById(String id);

    Optional<Client> findByDocument(String document);

    Optional<Client> findByEmail(String email);

    List<Client> findAll();

    void delete(String id);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);
}
