package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.validation.CpfCnpjValidator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerByDocument(String document) {
        String normalizedDocument = CpfCnpjValidator.validateAndNormalize(document);

        Optional<Customer> byDocument = customerRepository.findByDocument(normalizedDocument);
        if (byDocument.isPresent()) {
            log.debug("[DEBUG] - GET CUSTOMER: {}", byDocument);
            return byDocument;
        }
        throw new ResourceNotFoundException("Customer", "document", normalizedDocument);
    }

    public Optional<Customer> getCustomerById(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            return byId;
        }
        throw new ResourceNotFoundException("Customer", "id", id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
