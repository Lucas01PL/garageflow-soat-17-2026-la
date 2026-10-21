package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderApplicationService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.AverageWorkshopServiceExecutionTimeResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.CreateRepairOrderResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.RepairOrderMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.WorkshopServiceMonitoringResponseMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.presentation.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/repair-orders")
public class RepairOrderController extends BaseController {

    private final RepairOrderApplicationService applicationService;
    private final RepairOrderMapper mapper;
    private final WorkshopServiceMonitoringResponseMapper workshopServiceMonitoringResponseMapper;

    @Operation(
            summary = "Create Repair Order",
            description = "Creates a new repair order."
    )
    @PostMapping
    public ResponseEntity<CreateRepairOrderResponse> create(@Valid @RequestBody CreateRepairOrderRequest dto) {
        String userId = resolveCurrentUserId();
        RepairOrder ro = mapper.toModel(dto, userId);
        RepairOrder created = applicationService.create(ro);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toCreateRepairOrderResponse(created));
    }

    @Operation(
            summary = "Get Repair Order by ID",
            description = "Retrieves a repair order by its ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDTO> getById(@PathVariable String id) {
        Optional<RepairOrder> ro = applicationService.getById(id);
        return ro.map(repairOrder -> ResponseEntity.ok(mapper.toResponse(repairOrder)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "List Repair Orders",
            description = "Lists repair orders optionally filtered by status. If no status is provided, all repair orders will be listed."
    )
    @GetMapping
    public ResponseEntity<List<RepairOrderResponseDTO>> list(@RequestParam(required = false) String status) {
        List<RepairOrder> repairOrders = applicationService.list(status);
        List<RepairOrderResponseDTO> dtos = repairOrders.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Add Workshop Service to Repair Order",
            description = "Adds a workshop service to an existing repair order."
    )
    @PostMapping("/{repairOrderId}/workshop-services")
    public ResponseEntity<RepairOrderResponseDTO> addWorkshopService(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddWorkshopServiceRequest request) {

        RepairOrder repairOrder = applicationService.addWorkshopService(repairOrderId, request);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Add Part to Repair Order",
            description = "Adds a part to an existing repair order."
    )
    @PostMapping("/{repairOrderId}/parts")
    public ResponseEntity<RepairOrderResponseDTO> addPart(
            @PathVariable String repairOrderId,
            @Valid @RequestBody AddPartRequest request) {

        RepairOrder repairOrder = applicationService.addPart(repairOrderId, request);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Remove Workshop Service from Repair Order",
            description = "Removes a workshop service from an existing repair order."
    )
    @DeleteMapping("/{repairOrderId}/workshop-services/{workshopServiceId}")
    public ResponseEntity<RepairOrderResponseDTO> removeWorkshopService(
            @PathVariable String repairOrderId,
            @PathVariable String workshopServiceId,
            @Valid @RequestBody RemoveWorkshopServiceRequest request) {

        RepairOrder repairOrder = applicationService.removeWorkshopService(repairOrderId, workshopServiceId, request);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Remove Part from Repair Order",
            description = "Removes a part from an existing repair order."
    )
    @DeleteMapping("/{repairOrderId}/parts/{partId}")
    public ResponseEntity<RepairOrderResponseDTO> removePart(
            @PathVariable String repairOrderId,
            @PathVariable String partId,
            @Valid @RequestBody RemovePartRequest request) {

        RepairOrder repairOrder = applicationService.removePart(repairOrderId, partId, request);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to In Diagnosis",
            description = "Sets the status of an existing repair order to In Diagnosis."
    )
    @PatchMapping("/{repairOrderId}/status/in-diagnosis")
    public ResponseEntity<RepairOrderResponseDTO> inDiagnosis(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder = applicationService.startDiagnosis(repairOrderId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Start Workshop Service in Repair Order",
            description = "Starts a workshop service in an existing repair order."
    )
    @PatchMapping("/{repairOrderId}/workshop-services/{workshopServiceId}/status/in-execution")
    public ResponseEntity<RepairOrderResponseDTO> startWorkshopService(
            @PathVariable String repairOrderId,
            @PathVariable String workshopServiceId) {

        RepairOrder repairOrder = applicationService.startWorkshopService(repairOrderId, workshopServiceId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Finish Workshop Service in Repair Order",
            description = "Finishes a workshop service in an existing repair order."
    )
    @PatchMapping("/{repairOrderId}/workshop-services/{workshopServiceId}/status/finished")
    public ResponseEntity<RepairOrderResponseDTO> finishWorkshopService(
            @PathVariable String repairOrderId,
            @PathVariable String workshopServiceId,
            @Valid @RequestBody FinishWorkshopServiceRequest request) {

        RepairOrder repairOrder = applicationService.finishWorkshopService(repairOrderId, workshopServiceId, request);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Approved",
            description = "Sets the status of an existing repair order to Approved."
    )
    @PatchMapping("/{repairOrderId}/status/approved")
    public ResponseEntity<RepairOrderResponseDTO> approve(
            @PathVariable String repairOrderId) {

        String userId = resolveCurrentUserId();
        RepairOrder repairOrder = applicationService.approve(repairOrderId, userId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Rejected",
            description = "Sets the status of an existing repair order to Rejected."
    )
    @PatchMapping("/{repairOrderId}/status/rejected")
    public ResponseEntity<RepairOrderResponseDTO> reject(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder = applicationService.reject(repairOrderId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Delivered",
            description = "Sets the status of an existing repair order to Delivered."
    )
    @PatchMapping("/{repairOrderId}/status/delivered")
    public ResponseEntity<RepairOrderResponseDTO> deliver(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder = applicationService.deliver(repairOrderId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to In Execution",
            description = "Sets the status of an existing repair order to In Execution."
    )
    @PatchMapping("/{repairOrderId}/status/in-execution")
    public ResponseEntity<RepairOrderResponseDTO> startExecution(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder = applicationService.startExecution(repairOrderId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "Set Repair Order to Awaiting Approval",
            description = "Sets the status of an existing repair order to Awaiting Approval."
    )
    @PatchMapping("/{repairOrderId}/status/awaiting-approval")
    public ResponseEntity<RepairOrderResponseDTO> requestApproval(
            @PathVariable String repairOrderId) {

        RepairOrder repairOrder = applicationService.requestApproval(repairOrderId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }

    @Operation(
            summary = "List Repair Orders of the Authenticated Customer",
            description = "Retrieves the repair orders belonging to the customer linked to the authenticated user's email."
    )
    @GetMapping("/customer")
    public ResponseEntity<List<RepairOrderResponseDTO>> listByCustomer() {
        String currentEmail = resolveCurrentEmail();
        List<RepairOrder> repairOrders = applicationService.listByCustomerEmail(currentEmail);
        List<RepairOrderResponseDTO> response = repairOrders.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Average Workshop Service Execution Time",
            description = "Returns average execution time metrics for completed workshop services."
    )
    @GetMapping("/monitoring/workshop-services/average-execution-time")
    public ResponseEntity<List<AverageWorkshopServiceExecutionTimeResponse>>
    getAverageWorkshopServiceExecutionTime() {
        List<AverageWorkshopServiceExecutionTimeResponse> response =
                applicationService.getAverageWorkshopServiceExecutionTime()
                        .stream()
                        .map(workshopServiceMonitoringResponseMapper::toResponse)
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

        RepairOrder repairOrder = applicationService.cancel(repairOrderId);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }
}

