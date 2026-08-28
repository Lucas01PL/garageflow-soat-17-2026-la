package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer requestToCustomer(CustomerRequest customerRequest) {
        return new Customer(
                customerRequest.name(),
                customerRequest.document(),
                customerRequest.phone(),
                customerRequest.email(),
                customerRequest.address()
        );
    }

    public CustomerResponse customerToResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getDocument(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
