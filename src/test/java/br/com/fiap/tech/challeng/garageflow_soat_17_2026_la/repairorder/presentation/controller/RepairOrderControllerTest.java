package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderApplicationService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.CreateRepairOrderResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.RepairOrderMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairOrderControllerTest {

    @Mock
    private RepairOrderApplicationService applicationService;

    @Mock
    private RepairOrderMapper mapper;

    @InjectMocks
    private RepairOrderController controller;

    private RepairOrder repairOrder;
    private RepairOrderResponseDTO response;
    private CreateRepairOrderRequest createRequest;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder().id("repair-order-1").build();
        response = new RepairOrderResponseDTO();
        createRequest = new CreateRepairOrderRequest();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new AuthenticatedUser("user-123", "user@email.com"),
                        null,
                        List.of()
                )
        );
    }

    @Test
    void shouldCreateRepairOrderSuccessfully() {
        when(mapper.toModel(createRequest, "user-123")).thenReturn(repairOrder);
        when(applicationService.create(repairOrder)).thenReturn(repairOrder);
        when(mapper.toCreateRepairOrderResponse(repairOrder)).thenReturn(new CreateRepairOrderResponse("repair-order-1"));

        ResponseEntity<?> result = controller.create(createRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(applicationService).create(repairOrder);
    }

    @Test
    void shouldReturnBadRequestWhenUserIsNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> controller.create(createRequest));
        verifyNoInteractions(applicationService);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldGetRepairOrderByIdSuccessfully() {
        when(applicationService.getById("repair-order-1")).thenReturn(Optional.of(repairOrder));
        when(mapper.toResponse(repairOrder)).thenReturn(response);

        ResponseEntity<?> result = controller.getById("repair-order-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());
        verify(applicationService).getById("repair-order-1");
    }

    @Test
    void shouldReturnNotFoundWhenRepairOrderDoesNotExist() {
        when(applicationService.getById("repair-order-1")).thenReturn(Optional.empty());

        ResponseEntity<?> result = controller.getById("repair-order-1");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void shouldListAllRepairOrdersSuccessfully() {
        List<RepairOrder> repairOrders = List.of(RepairOrder.builder().id("repair-order-1").build());
        when(applicationService.list(null)).thenReturn(repairOrders);
        when(mapper.toResponse(repairOrders.getFirst())).thenReturn(response);

        ResponseEntity<List<RepairOrderResponseDTO>> result = controller.list(null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertSame(response, result.getBody().getFirst());
    }

    @Test
    void shouldStartRepairOrderExecutionSuccessfully() {
        when(applicationService.startExecution("repair-order-1")).thenReturn(repairOrder);
        when(mapper.toResponse(repairOrder)).thenReturn(response);

        ResponseEntity<?> result = controller.startExecution("repair-order-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());
        verify(applicationService).startExecution("repair-order-1");
    }
}
