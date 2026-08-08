package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
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
class GetClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private GetClientUseCase getClientUseCase;

    @Test
    void shouldReturnClientWhenDocumentExists() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        when(clientRepository.findByDocument("52998224725")).thenReturn(Optional.of(client));

        Optional<Client> result = getClientUseCase.getClientByDocument("529.982.247-25");

        assertTrue(result.isPresent());
        assertEquals("id-1", result.get().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDocumentDoesNotExist() {
        when(clientRepository.findByDocument("52998224725")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getClientUseCase.getClientByDocument("529.982.247-25"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenDocumentFormatIsInvalid() {
        assertThrows(InvalidDocumentException.class, () -> getClientUseCase.getClientByDocument("111.111.111-11"));
    }

    @Test
    void shouldReturnClientWhenIdExists() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        when(clientRepository.findById("id-1")).thenReturn(Optional.of(client));

        Optional<Client> result = getClientUseCase.getClientById("id-1");

        assertTrue(result.isPresent());
        assertEquals("52998224725", result.get().getDocument());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(clientRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getClientUseCase.getClientById("missing"));
    }
}
