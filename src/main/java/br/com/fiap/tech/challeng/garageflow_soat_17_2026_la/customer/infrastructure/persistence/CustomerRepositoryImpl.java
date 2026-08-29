package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMongoRepository customerMongoRepository;

    public CustomerRepositoryImpl(CustomerMongoRepository customerMongoRepository) {
        this.customerMongoRepository = customerMongoRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerDocument savedEntity = customerMongoRepository.save(toEntity(customer));
        return toCustomerDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return customerMongoRepository.findById(id).map(this::toCustomerDomain);
    }

    @Override
    public Optional<Customer> findByDocument(String document) {
        return customerMongoRepository.findByDocument(document).map(this::toCustomerDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerMongoRepository.findByEmail(email).map(this::toCustomerDomain);
    }

    @Override
    public List<Customer> findAll() {
        return customerMongoRepository.findAll()
                .stream()
                .map(this::toCustomerDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        customerMongoRepository.deleteById(id);
    }

    @Override
    public boolean existsByDocument(String document) {
        return customerMongoRepository.existsByDocument(document);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerMongoRepository.existsByEmail(email);
    }

    private Customer toCustomerDomain(CustomerDocument customerDocument) {
        return new Customer(
                customerDocument.getId(),
                customerDocument.getName(),
                customerDocument.getDocument(),
                customerDocument.getPhone(),
                customerDocument.getEmail(),
                customerDocument.getAddress()
        );
    }

    private CustomerDocument toEntity(Customer customer) {
        CustomerDocument document = new CustomerDocument(
                customer.getName(),
                customer.getDocument(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress()
        );
        document.setId(customer.getId());
        return document;
    }
}
