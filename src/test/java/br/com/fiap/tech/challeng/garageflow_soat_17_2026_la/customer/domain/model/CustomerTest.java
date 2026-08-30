package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithoutId() {
        Customer customer = new Customer("Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        assertNull(customer.getId());
        assertEquals("Joao Silva", customer.getName());
        assertEquals("52998224725", customer.getDocument());
        assertEquals("11999998888", customer.getPhone());
        assertEquals("joao@email.com", customer.getEmail());
        assertEquals("Rua A, 123", customer.getAddress());
    }

    @Test
    void shouldCreateCustomerWithId() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        assertEquals("id-1", customer.getId());
        assertEquals("52998224725", customer.getDocument());
    }

    @Test
    void shouldUpdateNamePhoneEmailAndAddress() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        customer.update("Joao S. Silva", "11988887777", "joao.silva@email.com", "Rua B, 456");

        assertEquals("Joao S. Silva", customer.getName());
        assertEquals("11988887777", customer.getPhone());
        assertEquals("joao.silva@email.com", customer.getEmail());
        assertEquals("Rua B, 456", customer.getAddress());
        assertEquals("id-1", customer.getId());
        assertEquals("52998224725", customer.getDocument());
    }

    @Test
    void toStringShouldContainFieldValues() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        assertTrue(customer.toString().contains("Joao Silva"));
        assertTrue(customer.toString().contains("52998224725"));
    }
}
