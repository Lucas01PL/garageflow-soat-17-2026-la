package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.domain.model.Customer;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.dto.CustomerResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.customer.presentation.mapper.CustomerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final CustomerMapper customerMapper;
    private final ListAllCustomersUseCase listAllCustomersUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase, GetCustomerUseCase getCustomerUseCase, UpdateCustomerUseCase updateUseCase, DeleteCustomerUseCase deleteCustomerUseCase, CustomerMapper customerMapper, ListAllCustomersUseCase listAllCustomersUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.customerMapper = customerMapper;
        this.listAllCustomersUseCase = listAllCustomersUseCase;
    }

    @Operation(
            summary = "Create Customer.",
            description = "Creates a new customer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Customer with the given document already exists")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createNewCustomer(@Valid @RequestBody CustomerRequest request) {
        Customer response = createCustomerUseCase.createCustomer(customerMapper.requestToCustomer(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(customerMapper.customerToResponse(response));
    }

    @Operation(
            summary = "Get Customer by id.",
            description = "Retrieves a customer by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        Optional<Customer> customerById = getCustomerUseCase.getCustomerById(id);
        CustomerResponse customerResponse = customerMapper.customerToResponse(customerById.get());
        return ResponseEntity.status(HttpStatus.OK).body(customerResponse);
    }

    @Operation(
            summary = "List Customers",
            description = "List customers optionally filtered by document. If no document is provided, all customers will be listed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found")
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listCustomers(@RequestParam(required = false) String document) {
        List<Customer> customers;

        if (document == null || document.isBlank()) {
            customers = listAllCustomersUseCase.findAll();
        } else {
            customers = getCustomerUseCase.getCustomerByDocument(document).stream().toList();
        }

        List<CustomerResponse> customerResponseList = customers.stream().map(customerMapper::customerToResponse).toList();
        return ResponseEntity.status(HttpStatus.OK).body(customerResponseList);
    }

    @Operation(
            summary = "Update Customer.",
            description = "Updates a customer by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@Valid @RequestBody CustomerRequest request, @PathVariable String id) {
        Customer updateCustomerWithId = updateUseCase.updateCustomerWithId(id, customerMapper.requestToCustomer(request));
        return ResponseEntity.status(HttpStatus.OK).body(customerMapper.customerToResponse(updateCustomerWithId));
    }

    @Operation(
            summary = "Delete Customer.",
            description = "Deletes a customer by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerResponse> deleteCustomer(@PathVariable String id) {
        deleteCustomerUseCase.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
