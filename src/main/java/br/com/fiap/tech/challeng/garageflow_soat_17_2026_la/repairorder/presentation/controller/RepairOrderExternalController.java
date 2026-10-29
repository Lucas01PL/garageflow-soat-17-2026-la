package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderApplicationService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.request.RepairOrderBudgetDecisionRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.RepairOrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/repair-orders")
public class RepairOrderExternalController {

    private final RepairOrderApplicationService applicationService;
    private final RepairOrderMapper mapper;

    @Operation(
            summary = "Decide a repair order budget",
            description = "Uses the one-time approval key sent to the customer to approve or reject a budget."
    )
    @PostMapping("/budget-decision")
    public ResponseEntity<RepairOrderResponseDTO> decideBudget(
            @Parameter(description = "One-time approval key sent to the customer")
            @RequestHeader("X-API-Key") String apiKey,
            @Valid @RequestBody RepairOrderBudgetDecisionRequest request) {

        RepairOrder repairOrder =
                applicationService.decideBudget(apiKey, request.approved());
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }
}
