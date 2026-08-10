package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
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
class CreateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    @Test
    void shouldCreateClientWhenDocumentIsValidAndNotDuplicated() {
        Client client = new Client("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");
        Client saved = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(clientRepository.existsByDocument("52998224725")).thenReturn(false);
        when(clientRepository.save(any())).thenReturn(saved);

        Client result = createClientUseCase.createClient(client);

        assertNotNull(result);
        assertEquals("id-1", result.getId());
        assertEquals("52998224725", result.getDocument());
    }

    @Test
    void shouldNormalizeDocumentBeforePersisting() {
        Client client = new Client("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");

        when(clientRepository.existsByDocument("52998224725")).thenReturn(false);
        when(clientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        createClientUseCase.createClient(client);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("52998224725", captor.getValue().getDocument());
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenDocumentIsInvalid() {
        Client client = new Client("Joao Silva", "111.111.111-11", "11999998888", "joao@email.com", "Rua A, 123");

        assertThrows(InvalidDocumentException.class, () -> createClientUseCase.createClient(client));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenDocumentAlreadyExists() {
        Client client = new Client("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");

        when(clientRepository.existsByDocument("52998224725")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> createClientUseCase.createClient(client));
        verify(clientRepository, never()).save(any());
    }
}
