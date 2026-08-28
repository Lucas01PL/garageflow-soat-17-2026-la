package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.validation.CpfCnpjValidator;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.DuplicateResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        String normalizedDocument = CpfCnpjValidator.validateAndNormalize(customer.getDocument());

        boolean isPresentWithDocument = customerRepository.existsByDocument(normalizedDocument);
        boolean isPresentWithEmail = customerRepository.existsByEmail(customer.getEmail());

        if (isPresentWithEmail) {
            log.debug("[DEBUG] - Trying to register duplicated customer with e-mail: {}", customer.getEmail());
            throw new DuplicateResourceException("Customer", "e-mail", customer.getEmail());
        }

        if (isPresentWithDocument) {
            log.debug("[DEBUG] - Trying to register duplicated customer with document: {}", normalizedDocument);
            throw new DuplicateResourceException("Customer", "document", normalizedDocument);
        }

        Customer normalizedCustomer = new Customer(
                customer.getName(),
                normalizedDocument,
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress()
        );

        log.debug("[DEBUG] - POST CUSTOMER: {}", normalizedCustomer);
        return customerRepository.save(normalizedCustomer);
    }
}
