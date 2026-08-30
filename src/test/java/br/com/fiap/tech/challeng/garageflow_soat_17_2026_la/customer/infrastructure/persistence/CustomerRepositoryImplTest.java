package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryImplTest {

    @Mock
    private CustomerMongoRepository customerMongoRepository;

    @InjectMocks
    private CustomerRepositoryImpl customerRepository;

    @Test
    void shouldSaveAndReturnDomain() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        CustomerDocument savedDocument = new CustomerDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        savedDocument.setId("id-1");

        when(customerMongoRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(savedDocument);

        Customer result = customerRepository.save(customer);

        assertEquals("id-1", result.getId());
        assertEquals("52998224725", result.getDocument());

        ArgumentCaptor<CustomerDocument> captor = ArgumentCaptor.forClass(CustomerDocument.class);
        verify(customerMongoRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
        assertEquals("52998224725", captor.getValue().getDocument());
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        CustomerDocument document = new CustomerDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        document.setId("id-1");
        when(customerMongoRepository.findById("id-1")).thenReturn(Optional.of(document));

        Optional<Customer> result = customerRepository.findById("id-1");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(customerMongoRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<Customer> result = customerRepository.findById("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindByDocumentAndMapToDomain() {
        CustomerDocument document = new CustomerDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        document.setId("id-1");
        when(customerMongoRepository.findByDocument("52998224725")).thenReturn(Optional.of(document));

        Optional<Customer> result = customerRepository.findByDocument("52998224725");

        assertTrue(result.isPresent());
        assertEquals("52998224725", result.get().getDocument());
    }

    @Test
    void shouldReturnEmptyWhenFindByDocumentNotFound() {
        when(customerMongoRepository.findByDocument("missing")).thenReturn(Optional.empty());

        Optional<Customer> result = customerRepository.findByDocument("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        CustomerDocument document1 = new CustomerDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        document1.setId("id-1");
        CustomerDocument document2 = new CustomerDocument("Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");
        document2.setId("id-2");
        when(customerMongoRepository.findAll()).thenReturn(List.of(document1, document2));

        List<Customer> result = customerRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("id-1", result.get(0).getId());
        assertEquals("id-2", result.get(1).getId());
    }

    @Test
    void shouldDeleteById() {
        customerRepository.delete("id-1");

        verify(customerMongoRepository).deleteById("id-1");
    }

    @Test
    void shouldDelegateExistsByDocument() {
        when(customerMongoRepository.existsByDocument("52998224725")).thenReturn(true);

        assertTrue(customerRepository.existsByDocument("52998224725"));

        when(customerMongoRepository.existsByDocument("11222333000181")).thenReturn(false);

        assertFalse(customerRepository.existsByDocument("11222333000181"));
    }
}
