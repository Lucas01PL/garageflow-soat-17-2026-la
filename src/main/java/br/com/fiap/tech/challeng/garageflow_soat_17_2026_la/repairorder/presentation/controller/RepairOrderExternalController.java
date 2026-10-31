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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    @PostMapping(
            value = "/budget-decision",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    "application/*+json"
            }
    )
    public ResponseEntity<RepairOrderResponseDTO> decideBudget(
            @Parameter(description = "One-time approval key sent to the customer")
            @RequestHeader("X-API-Key") String apiKey,
            @Valid @RequestBody RepairOrderBudgetDecisionRequest request) {
        return decideBudget(apiKey, request.approved());
    }

    @PostMapping(
            value = "/budget-decision",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<RepairOrderResponseDTO> decideBudgetFromEmail(
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "approved", required = false) Boolean approved) {
        return decideBudget(token, approved);
    }

    private ResponseEntity<RepairOrderResponseDTO> decideBudget(
            String approvalToken,
            Boolean approved) {
        if (approvalToken == null || approvalToken.isBlank() || approved == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Approval token and decision are required."
            );
        }

        RepairOrder repairOrder =
                applicationService.decideBudget(approvalToken, approved);
        return ResponseEntity.ok(mapper.toResponse(repairOrder));
    }
}
