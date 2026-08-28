package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.CreateCustomerUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.DeleteCustomerUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.GetCustomerUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.ListAllCustomersUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.UpdateCustomerUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CreateCustomerUseCase createCustomerUseCase;

    @Mock
    private GetCustomerUseCase getCustomerUseCase;

    @Mock
    private UpdateCustomerUseCase updateUseCase;

    @Mock
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private ListAllCustomersUseCase listAllCustomersUseCase;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void shouldCreateNewCustomer() {
        CustomerRequest request = new CustomerRequest("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");
        Customer customer = new Customer("Joao Silva", "529.982.247-25", "11999998888", "joao@email.com", "Rua A, 123");
        Customer createdCustomer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        CustomerResponse response = new CustomerResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(customerMapper.requestToCustomer(request)).thenReturn(customer);
        when(createCustomerUseCase.createCustomer(customer)).thenReturn(createdCustomer);
        when(customerMapper.customerToResponse(createdCustomer)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.createNewCustomer(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetCustomerById() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        CustomerResponse response = new CustomerResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(getCustomerUseCase.getCustomerById("id-1")).thenReturn(Optional.of(customer));
        when(customerMapper.customerToResponse(customer)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.getCustomerById("id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetCustomerByDocument() {
        Customer customer = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        CustomerResponse response = new CustomerResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");

        when(getCustomerUseCase.getCustomerByDocument("52998224725")).thenReturn(Optional.of(customer));
        when(customerMapper.customerToResponse(customer)).thenReturn(response);

        ResponseEntity<List<CustomerResponse>> result = customerController.listCustomers("52998224725");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody().getFirst());
    }

    @Test
    void shouldGetAllCustomers() {
        Customer customer1 = new Customer("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        Customer customer2 = new Customer("id-2", "Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");
        CustomerResponse response1 = new CustomerResponse("id-1", "Joao Silva", "52998224725", "11999998888", "joao@email.com", "Rua A, 123");
        CustomerResponse response2 = new CustomerResponse("id-2", "Empresa XPTO", "11222333000181", "1132221111", "contato@xpto.com", "Av. B, 789");

        when(listAllCustomersUseCase.findAll()).thenReturn(List.of(customer1, customer2));
        when(customerMapper.customerToResponse(customer1)).thenReturn(response1);
        when(customerMapper.customerToResponse(customer2)).thenReturn(response2);

        ResponseEntity<List<CustomerResponse>> result = customerController.listCustomers(null);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void shouldUpdateCustomer() {
        CustomerRequest request = new CustomerRequest("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        Customer mappedCustomer = new Customer("Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        Customer updatedCustomer = new Customer("id-1", "Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");
        CustomerResponse response = new CustomerResponse("id-1", "Joao S. Silva", "52998224725", "11988887777", "joao.silva@email.com", "Rua B, 456");

        when(customerMapper.requestToCustomer(request)).thenReturn(mappedCustomer);
        when(updateUseCase.updateCustomerWithId("id-1", mappedCustomer)).thenReturn(updatedCustomer);
        when(customerMapper.customerToResponse(updatedCustomer)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.updateCustomer(request, "id-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldDeleteCustomer() {
        ResponseEntity<CustomerResponse> result = customerController.deleteCustomer("id-1");

        assertEquals(204, result.getStatusCode().value());
    }
}
