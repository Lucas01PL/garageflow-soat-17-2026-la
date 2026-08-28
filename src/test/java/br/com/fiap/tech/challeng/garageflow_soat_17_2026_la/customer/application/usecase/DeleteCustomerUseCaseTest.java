package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Test
    void shouldDeleteCustomerWhenExists() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        when(customerRepository.findById("id-1")).thenReturn(Optional.of(customer));

        deleteCustomerUseCase.deleteCustomer("id-1");

        verify(customerRepository).delete("id-1");
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerDoesNotExist() {
        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deleteCustomerUseCase.deleteCustomer("missing"));
        verify(customerRepository, never()).delete(anyString());
    }
}
