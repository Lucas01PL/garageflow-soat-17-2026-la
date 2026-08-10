package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.application.usecase;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
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
class ListAllClientsUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ListAllClientsUseCase listAllClientsUseCase;

    @Test
    void shouldReturnAllClients() {
        Client client1 = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        Client client2 = new Client("id-2", "Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");
        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<Client> result = listAllClientsUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoClientsExist() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        List<Client> result = listAllClientsUseCase.findAll();

        assertTrue(result.isEmpty());
    }
}
