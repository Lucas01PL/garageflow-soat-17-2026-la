package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.repository.CustomerRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.DuplicateResourceException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.InvalidDocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    @Test
    void shouldCreateCustomerWhenDocumentIsValidAndNotDuplicated() {
        Customer customer = new Customer("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");
        Customer saved = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(customerRepository.existsByDocument("52998224725")).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(saved);

        Customer result = createCustomerUseCase.createCustomer(customer);

        assertNotNull(result);
        assertEquals("id-1", result.getId());
        assertEquals("52998224725", result.getDocument());
    }

    @Test
    void shouldNormalizeDocumentBeforePersisting() {
        Customer customer = new Customer("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");

        when(customerRepository.existsByDocument("52998224725")).thenReturn(false);
        when(customerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        createCustomerUseCase.createCustomer(customer);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        assertEquals("52998224725", captor.getValue().getDocument());
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenDocumentIsInvalid() {
        Customer customer = new Customer("Joao Silva", "111.111.111-11", "11999998888", "joao@email.com", "Rua A, 123");

        assertThrows(InvalidDocumentException.class, () -> createCustomerUseCase.createCustomer(customer));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenDocumentAlreadyExists() {
        Customer customer = new Customer("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");

        when(customerRepository.existsByDocument("52998224725")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> createCustomerUseCase.createCustomer(customer));
        verify(customerRepository, never()).save(any());
    }
}
