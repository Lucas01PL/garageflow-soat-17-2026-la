package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.*;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairOrderControllerTest {

    @Mock
    private CreateRepairOrderUseCase createUseCase;

    @Mock
    private GetRepairOrderByIdUseCase getByIdUseCase;

    @Mock
    private ListAllRepairOrdersUseCase listAllUseCase;

    @Mock
    private AddWorkshopServiceUseCase addWorkshopServiceUseCase;

    @Mock
    private AddPartUseCase addPartUseCase;

    @Mock
    private RemovePartUseCase removePartUseCase;

    @Mock
    private RemoveWorkshopServiceUseCase removeWorkshopServiceUseCase;

    @Mock
    private StartRepairOrderDiagnosisUseCase startRepairOrderDiagnosisUseCase;

    @Mock
    private StartWorkshopServiceUseCase startWorkshopServiceUseCase;

    @Mock
    private FinishWorkshopServiceUseCase finishWorkshopServiceUseCase;

    @Mock
    private RepairOrderMapper mapper;

    @Mock
    private ApproveRepairOrderUseCase approveRepairOrderUseCase;

    @Mock
    private RejectRepairOrderUseCase rejectRepairOrderUseCase;

    @Mock
    private DeliverRepairOrderUseCase deliverRepairOrderUseCase;

    @Mock
    private StartRepairOrderExecutionUseCase startRepairOrderExecutionUseCase;

    @Mock
    private RequestRepairOrderApprovalUseCase requestRepairOrderApprovalUseCase;

    @Mock
    private CancelRepairOrderUseCase cancelRepairOrderUseCase;

    @InjectMocks
    private RepairOrderController controller;

    private RepairOrder repairOrder;
    private RepairOrderResponseDTO response;
    private CreateRepairOrderRequest createRequest;
    private AddWorkshopServiceRequest workshopServiceRequest;
    private RemoveWorkshopServiceRequest removeWorkshopServiceRequest;
    private AddPartRequest partRequest;
    private RemovePartRequest partRemoveRequest;
    private FinishWorkshopServiceRequest finishRequest;

    @BeforeEach
    void setUp() {
        repairOrder = RepairOrder.builder()
                .id("repair-order-1")
                .build();

        response = new RepairOrderResponseDTO();

        createRequest = new CreateRepairOrderRequest();
        workshopServiceRequest = new AddWorkshopServiceRequest();
        partRequest = new AddPartRequest();
        partRemoveRequest = new RemovePartRequest();
        finishRequest = new FinishWorkshopServiceRequest();

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

        RepairOrder model = repairOrder;
        RepairOrder created = repairOrder;

        when(mapper.toModel(createRequest, "user-123"))
                .thenReturn(model);

        when(createUseCase.execute(model))
                .thenReturn(created);

        when(mapper.toResponse(created))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.create(createRequest);

        assertEquals(
                HttpStatus.CREATED,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(mapper)
                .toModel(createRequest, "user-123");

        verify(createUseCase)
                .execute(model);

        verify(mapper)
                .toResponse(created);
    }

    @Test
    void shouldReturnBadRequestWhenCreateThrowsIllegalArgumentException() {

        when(mapper.toModel(createRequest, "user-123"))
                .thenReturn(repairOrder);

        when(createUseCase.execute(repairOrder))
                .thenThrow(
                        new IllegalArgumentException(
                                "Invalid repair order"
                        )
                );

        ResponseEntity<?> result =
                controller.create(createRequest);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                result.getStatusCode()
        );

        assertEquals(
                "Invalid repair order",
                result.getBody()
        );

        verify(createUseCase)
                .execute(repairOrder);

        verify(mapper, never())
                .toResponse(any());
    }

    @Test
    void shouldReturnBadRequestWhenUserIsNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);

        ResponseEntity<?> result = controller.create(createRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not authenticated", result.getBody());

        verifyNoInteractions(createUseCase);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldGetRepairOrderByIdSuccessfully() {

        when(getByIdUseCase.execute("repair-order-1"))
                .thenReturn(Optional.of(repairOrder));

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.getById("repair-order-1");

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(getByIdUseCase)
                .execute("repair-order-1");

        verify(mapper)
                .toResponse(repairOrder);
    }

    @Test
    void shouldReturnNotFoundWhenRepairOrderDoesNotExist() {

        when(getByIdUseCase.execute("repair-order-1"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> result =
                controller.getById("repair-order-1");

        assertEquals(
                HttpStatus.NOT_FOUND,
                result.getStatusCode()
        );

        assertNull(result.getBody());

        verify(getByIdUseCase)
                .execute("repair-order-1");

        verifyNoInteractions(mapper);
    }

    @Test
    void shouldReturnBadRequestWhenGetByIdThrowsIllegalArgumentException() {

        when(getByIdUseCase.execute(" "))
                .thenThrow(
                        new IllegalArgumentException(
                                "repairOrderId cannot be empty"
                        )
                );

        ResponseEntity<?> result =
                controller.getById(" ");

        assertEquals(
                HttpStatus.BAD_REQUEST,
                result.getStatusCode()
        );

        assertEquals(
                "repairOrderId cannot be empty",
                result.getBody()
        );

        verify(getByIdUseCase)
                .execute(" ");
    }

    @Test
    void shouldListAllRepairOrdersSuccessfully() {

        List<RepairOrder> repairOrders =
                List.of(
                        RepairOrder.builder()
                                .id("repair-order-1")
                                .build(),
                        RepairOrder.builder()
                                .id("repair-order-2")
                                .build()
                );

        RepairOrderResponseDTO firstResponse =
                new RepairOrderResponseDTO();

        RepairOrderResponseDTO secondResponse =
                new RepairOrderResponseDTO();

        when(listAllUseCase.execute())
                .thenReturn(repairOrders);

        when(mapper.toResponse(repairOrders.get(0)))
                .thenReturn(firstResponse);

        when(mapper.toResponse(repairOrders.get(1)))
                .thenReturn(secondResponse);

        ResponseEntity<List<RepairOrderResponseDTO>> result =
                controller.listAll();

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertNotNull(result.getBody());

        assertEquals(
                2,
                result.getBody().size()
        );

        assertSame(
                firstResponse,
                result.getBody().get(0)
        );

        assertSame(
                secondResponse,
                result.getBody().get(1)
        );

        verify(listAllUseCase)
                .execute();

        verify(mapper)
                .toResponse(repairOrders.get(0));

        verify(mapper)
                .toResponse(repairOrders.get(1));
    }

    @Test
    void shouldAddWorkshopServiceSuccessfully() {

        when(addWorkshopServiceUseCase.execute(
                "repair-order-1",
                workshopServiceRequest
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.addWorkshopService(
                        "repair-order-1",
                        workshopServiceRequest
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(addWorkshopServiceUseCase)
                .execute(
                        "repair-order-1",
                        workshopServiceRequest
                );

        verify(mapper)
                .toResponse(repairOrder);
    }

    @Test
    void shouldAddPartSuccessfully() {

        when(addPartUseCase.execute(
                "repair-order-1",
                partRequest
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.addPart(
                        "repair-order-1",
                        partRequest
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(addPartUseCase)
                .execute(
                        "repair-order-1",
                        partRequest
                );

        verify(mapper)
                .toResponse(repairOrder);
    }

    @Test
    void shouldRemoveWorkshopServiceSuccessfully() {

        when(removeWorkshopServiceUseCase.execute(
                "repair-order-1",
                "workshop-service-1",
                removeWorkshopServiceRequest
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.removeWorkshopService(
                        "repair-order-1",
                        "workshop-service-1",
                        removeWorkshopServiceRequest
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(removeWorkshopServiceUseCase)
                .execute(
                        "repair-order-1",
                        "workshop-service-1",
                        removeWorkshopServiceRequest
                );
    }

    @Test
    void shouldRemovePartSuccessfully() {

        when(removePartUseCase.execute(
                "repair-order-1",
                "partId",
                partRemoveRequest
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.removePart(
                        "repair-order-1",
                        "partId",
                        partRemoveRequest
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(removePartUseCase)
                .execute(
                        "repair-order-1",
                        "partId",
                        partRemoveRequest
                );
    }

    @Test
    void shouldStartRepairOrderDiagnosisSuccessfully() {

        when(startRepairOrderDiagnosisUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.inDiagnosis(
                        "repair-order-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(startRepairOrderDiagnosisUseCase)
                .execute("repair-order-1");
    }

    @Test
    void shouldStartWorkshopServiceSuccessfully() {

        when(startWorkshopServiceUseCase.execute(
                "repair-order-1",
                "workshop-service-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.startWorkshopService(
                        "repair-order-1",
                        "workshop-service-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(startWorkshopServiceUseCase)
                .execute(
                        "repair-order-1",
                        "workshop-service-1"
                );
    }

    @Test
    void shouldFinishWorkshopServiceSuccessfully() {

        when(finishWorkshopServiceUseCase.execute(
                "repair-order-1",
                "workshop-service-1",
                finishRequest
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.finishWorkshopService(
                        "repair-order-1",
                        "workshop-service-1",
                        finishRequest
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(finishWorkshopServiceUseCase)
                .execute(
                        "repair-order-1",
                        "workshop-service-1",
                        finishRequest
                );
    }

    @Test
    void shouldApproveRepairOrderSuccessfully() {

        when(approveRepairOrderUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.approve(
                        "repair-order-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(approveRepairOrderUseCase)
                .execute("repair-order-1");
    }

    @Test
    void shouldRejectRepairOrderSuccessfully() {

        when(rejectRepairOrderUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.reject(
                        "repair-order-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(rejectRepairOrderUseCase)
                .execute("repair-order-1");
    }

    @Test
    void shouldDeliverRepairOrderSuccessfully() {

        when(deliverRepairOrderUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.deliver(
                        "repair-order-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(deliverRepairOrderUseCase)
                .execute("repair-order-1");
    }

    @Test
    void shouldStartRepairOrderExecutionSuccessfully() {

        when(startRepairOrderExecutionUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.startExecution(
                        "repair-order-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(startRepairOrderExecutionUseCase)
                .execute("repair-order-1");
    }

    @Test
    void shouldRequestRepairOrderApprovalSuccessfully() {

        when(requestRepairOrderApprovalUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<?> result =
                controller.requestApproval(
                        "repair-order-1"
                );

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(requestRepairOrderApprovalUseCase)
                .execute("repair-order-1");
    }

    @Test
    void shouldCancelRepairOrderSuccessfully() {

        when(cancelRepairOrderUseCase.execute(
                "repair-order-1"
        )).thenReturn(repairOrder);

        when(mapper.toResponse(repairOrder))
                .thenReturn(response);

        ResponseEntity<RepairOrderResponseDTO> result =
                controller.cancel("repair-order-1");

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                response,
                result.getBody()
        );

        verify(cancelRepairOrderUseCase)
                .execute("repair-order-1");

        verify(mapper)
                .toResponse(repairOrder);
    }
}