package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.infrastructure.persistence;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
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
class ClientRepositoryImplTest {

    @Mock
    private ClientMongoRepository clientMongoRepository;

    @InjectMocks
    private ClientRepositoryImpl clientRepository;

    @Test
    void shouldSaveAndReturnDomain() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        ClientDocument savedDocument = new ClientDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        savedDocument.setId("id-1");

        when(clientMongoRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(savedDocument);

        Client result = clientRepository.save(client);

        assertEquals("id-1", result.getId());
        assertEquals("52998224725", result.getDocument());

        ArgumentCaptor<ClientDocument> captor = ArgumentCaptor.forClass(ClientDocument.class);
        verify(clientMongoRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
        assertEquals("52998224725", captor.getValue().getDocument());
    }

    @Test
    void shouldFindByIdAndMapToDomain() {
        ClientDocument document = new ClientDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        document.setId("id-1");
        when(clientMongoRepository.findById("id-1")).thenReturn(Optional.of(document));

        Optional<Client> result = clientRepository.findById("id-1");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {
        when(clientMongoRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<Client> result = clientRepository.findById("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindByDocumentAndMapToDomain() {
        ClientDocument document = new ClientDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        document.setId("id-1");
        when(clientMongoRepository.findByDocument("52998224725")).thenReturn(Optional.of(document));

        Optional<Client> result = clientRepository.findByDocument("52998224725");

        assertTrue(result.isPresent());
        assertEquals("52998224725", result.get().getDocument());
    }

    @Test
    void shouldReturnEmptyWhenFindByDocumentNotFound() {
        when(clientMongoRepository.findByDocument("missing")).thenReturn(Optional.empty());

        Optional<Client> result = clientRepository.findByDocument("missing");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllAndMapToDomainList() {
        ClientDocument document1 = new ClientDocument("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        document1.setId("id-1");
        ClientDocument document2 = new ClientDocument("Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");
        document2.setId("id-2");
        when(clientMongoRepository.findAll()).thenReturn(List.of(document1, document2));

        List<Client> result = clientRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("id-1", result.get(0).getId());
        assertEquals("id-2", result.get(1).getId());
    }

    @Test
    void shouldDeleteById() {
        clientRepository.delete("id-1");

        verify(clientMongoRepository).deleteById("id-1");
    }

    @Test
    void shouldDelegateExistsByDocument() {
        when(clientMongoRepository.existsByDocument("52998224725")).thenReturn(true);

        assertTrue(clientRepository.existsByDocument("52998224725"));

        when(clientMongoRepository.existsByDocument("11222333000181")).thenReturn(false);

        assertFalse(clientRepository.existsByDocument("11222333000181"));
    }
}
