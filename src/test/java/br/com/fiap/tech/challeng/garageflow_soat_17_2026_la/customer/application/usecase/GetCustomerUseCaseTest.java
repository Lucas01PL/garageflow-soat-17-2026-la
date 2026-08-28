package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidDocumentException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private GetCustomerUseCase getCustomerUseCase;

    @Test
    void shouldReturnCustomerWhenDocumentExists() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        when(customerRepository.findByDocument("52998224725")).thenReturn(Optional.of(customer));

        Optional<Customer> result = getCustomerUseCase.getCustomerByDocument("529.982.247-25");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDocumentDoesNotExist() {
        when(customerRepository.findByDocument("52998224725")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getCustomerUseCase.getCustomerByDocument("529.982.247-25"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenDocumentFormatIsInvalid() {
        assertThrows(InvalidDocumentException.class, () -> getCustomerUseCase.getCustomerByDocument("111.111.111-11"));
    }

    @Test
    void shouldReturnCustomerWhenIdExists() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        when(customerRepository.findById("id-1")).thenReturn(Optional.of(customer));

        Optional<Customer> result = getCustomerUseCase.getCustomerById("id-1");

        assertTrue(result.isPresent());
        assertEquals("52998224725", result.get().getDocument());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getCustomerUseCase.getCustomerById("missing"));
    }
}
