package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddRemovePartRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.AddRemoveWorkshopServiceRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.CreateRepairOrderRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.FinishWorkshopServiceRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.AverageWorkshopServiceExecutionTimeResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.RepairOrderMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.WorkshopServiceMonitoringResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/repairorder")
public class RepairOrderController {

    private CreateRepairOrderUseCase createUseCase;

    private GetRepairOrderByIdUseCase getByIdUseCase;

    private ListAllRepairOrdersUseCase listAllUseCase;

    private AddWorkshopServiceUseCase addWorkshopServiceUseCase;

    private AddPartUseCase addPartUseCase;

    private RemovePartUseCase removePartUseCase;

    private RemoveWorkshopServiceUseCase removeWorkshopServiceUseCase;

    private StartRepairOrderDiagnosisUseCase startRepairOrderDiagnosisUseCase;

    private StartWorkshopServiceUseCase startWorkshopServiceUseCase;

    private FinishWorkshopServiceUseCase finishWorkshopServiceUseCase;

    private RepairOrderMapper mapper;

    private ApproveRepairOrderUseCase approveRepairOrderUseCase;

    private RejectRepairOrderUseCase rejectRepairOrderUseCase;

    private DeliverRepairOrderUseCase deliverRepairOrderUseCase;

    private StartRepairOrderExecutionUseCase startRepairOrderExecutionUseCase;

    private RequestRepairOrderApprovalUseCase requestRepairOrderApprovalUseCase;

    private ListRepairOrdersByCustomerUseCase listRepairOrdersByCustomerUseCase;

    private GetAverageWorkshopServiceExecutionTimeUseCase averageWorkshopServiceExecutionTimeUseCase;

    private WorkshopServiceMonitoringResponseMapper workshopServiceMonitoringResponseMapper;

    private CancelRepairOrderUseCase cancelRepairOrderUseCase;

    @Operation(
            summary = "Create Repair Order",
            description = "Creates a new repair order."
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRepairOrderRequest dto) {
        try {
            RepairOrder ro = mapper.toModel(dto);
            RepairOrder created = createUseCase.execute(ro);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Get Repair Order by ID",
            description = "Retrieves a repair order by its ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<RepairOrder> ro = getByIdUseCase.execute(id);
            return ro.map(repairOrder -> ResponseEntity.ok(mapper.toResponse(repairOrder)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "List All Repair Orders",
            description = "Retrieves a list of all repair orders."
    )
    @GetMapping
    public ResponseEntity<List<RepairOrderResponseDTO>> listAll() {
        List<RepairOrder> list = listAllUseCase.execute();
        List<RepairOrderResponseDTO> dtos = list.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Add Workshop Service to Repair Order",
            description = "Adds a workshop service to an existing repair order."
    )
    @PostMapping("/{repairOrderId}/services")
    public ResponseEntity<RepairOrderResponseDTO> addWorkshopService(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddRemoveWorkshopServiceRequest request) {

        RepairOrder repairOrder =
                addWorkshopServiceUseCase.execute(repairOrderId, request);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Add Part to Repair Order",
            description = "Adds a part to an existing repair order."
    )
    @PostMapping("/{repairOrderId}/parts")
    public ResponseEntity<RepairOrderResponseDTO> addPart(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddRemovePartRequest request) {

        RepairOrder repairOrder =
                addPartUseCase.execute(repairOrderId, request);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Remove Workshop Service from Repair Order",
            description = "Removes a workshop service from an existing repair order."
    )
    @DeleteMapping("/{repairOrderId}/services")
    public ResponseEntity<RepairOrderResponseDTO> removeWorkshopService(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddRemoveWorkshopServiceRequest request) {

        RepairOrder repairOrder =
                removeWorkshopServiceUseCase.execute(repairOrderId, request);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Remove Part from Repair Order",
            description = "Removes a part from an existing repair order."
    )
    @DeleteMapping("/{repairOrderId}/parts")
    public ResponseEntity<RepairOrderResponseDTO> removePart(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddRemovePartRequest request) {

        RepairOrder repairOrder =
                removePartUseCase.execute(repairOrderId, request);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to In Diagnosis",
            description = "Sets the status of an existing repair order to In Diagnosis."
    )
    @PatchMapping("/{repairOrderId}/status/in-diagnosis")
    public ResponseEntity<RepairOrderResponseDTO> inDiagnosis(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                startRepairOrderDiagnosisUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Start Workshop Service in Repair Order",
            description = "Starts a workshop service in an existing repair order."
    )
    @PatchMapping("/{repairOrderId}/services/{workshopServiceId}/status/start")
    public ResponseEntity<RepairOrderResponseDTO> startWorkshopService(
            @PathVariable String repairOrderId,
            @PathVariable String workshopServiceId) {

        RepairOrder repairOrder =
                startWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId);

        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Finish Workshop Service in Repair Order",
            description = "Finishes a workshop service in an existing repair order."
    )
    @PatchMapping("/{repairOrderId}/services/{workshopServiceId}/status/finished")
    public ResponseEntity<RepairOrderResponseDTO> finishWorkshopService(
            @PathVariable String repairOrderId,
            @PathVariable String workshopServiceId,
            @Valid @RequestBody FinishWorkshopServiceRequest request) {

        RepairOrder repairOrder =
                finishWorkshopServiceUseCase.execute(repairOrderId, workshopServiceId, request);

        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Approved",
            description = "Sets the status of an existing repair order to Approved."
    )
    @PatchMapping("/{repairOrderId}/status/approved")
    public ResponseEntity<RepairOrderResponseDTO> approve(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                approveRepairOrderUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Rejected",
            description = "Sets the status of an existing repair order to Rejected."
    )
    @PatchMapping("/{repairOrderId}/status/rejected")
    public ResponseEntity<RepairOrderResponseDTO> reject(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                rejectRepairOrderUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Delivered",
            description = "Sets the status of an existing repair order to Delivered."
    )
    @PatchMapping("/{repairOrderId}/status/deliver")
    public ResponseEntity<RepairOrderResponseDTO> deliver(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                deliverRepairOrderUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to In Execution",
            description = "Sets the status of an existing repair order to In Execution."
    )
    @PatchMapping("/{repairOrderId}/status/in-execution")
    public ResponseEntity<RepairOrderResponseDTO> startExecution(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                startRepairOrderExecutionUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Awaiting Approval",
            description = "Sets the status of an existing repair order to Awaiting Approval."
    )
    @PatchMapping("/{repairOrderId}/status/awaiting-approval")
    public ResponseEntity<RepairOrderResponseDTO> requestApproval(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                requestRepairOrderApprovalUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RepairOrderResponseDTO>> listByCustomer(
            @PathVariable String customerId) {

        List<RepairOrder> repairOrders =
                listRepairOrdersByCustomerUseCase.execute(customerId);

        List<RepairOrderResponseDTO> response =
                repairOrders.stream()
                        .map(mapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Average Workshop Service Execution Time",
            description = "Returns average execution time metrics for completed workshop services."
    )
    @GetMapping("/monitoring/services/average-execution-time")
    public ResponseEntity<List<AverageWorkshopServiceExecutionTimeResponse>>
    getAverageWorkshopServiceExecutionTime() {

        List<AverageWorkshopServiceExecutionTimeResponse> response =
                averageWorkshopServiceExecutionTimeUseCase.execute()
                        .stream()
                        .map(
                                workshopServiceMonitoringResponseMapper::toResponse
                        )
                        .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cancel Repair Order",
            description = "Cancels a repair order that has not started execution."
    )
    @PatchMapping("/{repairOrderId}/status/cancelled")
    public ResponseEntity<RepairOrderResponseDTO> cancel(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder =
                cancelRepairOrderUseCase.execute(repairOrderId);

        return ResponseEntity.ok(
                mapper.toResponse(repairOrder)
        );
    }
}

