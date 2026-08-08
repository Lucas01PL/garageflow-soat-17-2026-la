package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
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
class UpdateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private UpdateClientUseCase updateClientUseCase;

    @Test
    void shouldUpdateExistingClient() {
        Client existingClient = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        Client updatedData = new Client("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");

        when(clientRepository.findById("id-1")).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client result = updateClientUseCase.updateClientWithId("id-1", updatedData);

        assertEquals("Joao S. Silva", result.getName());
        assertEquals("11988887777", result.getPhone());
        assertEquals("joao.silva@email.com", result.getEmail());
        assertEquals("Rua B, 456", result.getAddress());
        assertEquals("52998224725", result.getDocument());

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("id-1", captor.getValue().getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenClientDoesNotExist() {
        Client updatedData = new Client("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        when(clientRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateClientUseCase.updateClientWithId("missing", updatedData));
    }
}
