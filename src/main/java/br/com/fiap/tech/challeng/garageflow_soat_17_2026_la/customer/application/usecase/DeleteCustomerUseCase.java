package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void deleteCustomer(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            log.debug("[DEBUG] - DELETED CUSTOMER: {}", byId.get());
            customerRepository.delete(id);
            return;
        }
        log.debug("[DEBUG] - Trying to delete non-existant customer with id: {}", id);
        throw new ResourceNotFoundException("Customer", "id", id);
    }
}
