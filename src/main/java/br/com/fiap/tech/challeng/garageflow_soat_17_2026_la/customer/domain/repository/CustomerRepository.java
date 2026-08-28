package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(String id);

    Optional<Customer> findByDocument(String document);

    Optional<Customer> findByEmail(String email);

    List<Customer> findAll();

    void delete(String id);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);
}
