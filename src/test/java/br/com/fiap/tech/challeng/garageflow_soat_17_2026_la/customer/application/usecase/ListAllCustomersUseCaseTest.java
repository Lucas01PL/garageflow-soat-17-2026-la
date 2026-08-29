package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllCustomersUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ListAllCustomersUseCase listAllCustomersUseCase;

    @Test
    void shouldReturnAllCustomers() {
        Customer customer1 = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        Customer customer2 = new Customer("id-2", "Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> result = listAllCustomersUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomersExist() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Customer> result = listAllCustomersUseCase.findAll();

        assertTrue(result.isEmpty());
    }
}
