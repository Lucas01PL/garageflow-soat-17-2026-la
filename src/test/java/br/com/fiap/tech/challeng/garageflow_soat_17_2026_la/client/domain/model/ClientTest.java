package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientTest {

    @Test
    void shouldCreateClientWithoutId() {
        Client client = new Client("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        assertNull(client.getId());
        assertEquals("Joao Silva", client.getName());
        assertEquals("52998224725", client.getDocument());
        assertEquals("11999998888", client.getPhone());
        assertEquals("joao@email.com", client.getEmail());
        assertEquals("Rua A, 123", client.getAddress());
    }

    @Test
    void shouldCreateClientWithId() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        assertEquals("id-1", client.getId());
        assertEquals("52998224725", client.getDocument());
    }

    @Test
    void shouldUpdateNamePhoneEmailAndAddress() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        client.update("Joao S. Silva", "11988887777", "joao.silva@email.com", "Rua B, 456");

        assertEquals("Joao S. Silva", client.getName());
        assertEquals("11988887777", client.getPhone());
        assertEquals("joao.silva@email.com", client.getEmail());
        assertEquals("Rua B, 456", client.getAddress());
        assertEquals("id-1", client.getId());
        assertEquals("52998224725", client.getDocument());
    }

    @Test
    void toStringShouldContainFieldValues() {
        Client client = new Client("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        assertTrue(client.toString().contains("Joao Silva"));
        assertTrue(client.toString().contains("52998224725"));
    }
}
