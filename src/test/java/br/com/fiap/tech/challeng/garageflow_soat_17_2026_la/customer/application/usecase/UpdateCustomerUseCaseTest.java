package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Test
    void shouldUpdateExistingCustomer() {
        Customer existingCustomer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        Customer updatedData = new Customer("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");

        when(customerRepository.findById("id-1")).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        Customer result = updateCustomerUseCase.updateCustomerWithId("id-1", updatedData);

        assertEquals("Joao S. Silva", result.getName());
        assertEquals("11988887777", result.getPhone());
        assertEquals("joao.silva@email.com", result.getEmail());
        assertEquals("Rua B, 456", result.getAddress());
        assertEquals("52998224725", result.getDocument());

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerDoesNotExist() {
        Customer updatedData = new Customer("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateCustomerUseCase.updateCustomerWithId("missing", updatedData));
    }
}
