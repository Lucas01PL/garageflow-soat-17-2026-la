package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.mapper;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void shouldMapRequestToCustomer() {
        CustomerRequest request = new CustomerRequest("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        Customer customer = customerMapper.requestToCustomer(request);

        assertNull(customer.getId());
        assertEquals("Joao Silva", customer.getName());
        assertEquals("52998224725", customer.getDocument());
        assertEquals("11999998888", customer.getPhone());
        assertEquals("joao@email.com", customer.getEmail());
        assertEquals("Rua A, 123", customer.getAddress());
    }

    @Test
    void shouldMapCustomerToResponse() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        CustomerResponse response = customerMapper.customerToResponse(customer);

        assertEquals("id-1", response.id());
        assertEquals("Joao Silva", response.name());
        assertEquals("52998224725", response.document());
        assertEquals("11999998888", response.phone());
        assertEquals("joao@email.com", response.email());
        assertEquals("Rua A, 123", response.address());
    }
}
