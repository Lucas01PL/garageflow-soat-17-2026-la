package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer updateCustomerWithId(String id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        existingCustomer.update(
                updatedCustomer.getName(),
                updatedCustomer.getPhone(),
                updatedCustomer.getEmail(),
                updatedCustomer.getAddress()
        );

        log.debug("[DEBUG] - UPDATED CUSTOMER: {}", existingCustomer);
        return customerRepository.save(existingCustomer);
    }
}
