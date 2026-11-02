package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderApplicationService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.BudgetDecisionResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/repair-orders")
public class RepairOrderExternalController {

    private final RepairOrderApplicationService applicationService;

    @Operation(
            summary = "Decide a repair order budget",
            description = "Uses the one-time approval key sent to the customer to approve or reject a budget."
    )
    @PostMapping(
            value = "/budget-decision",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<BudgetDecisionResponse> decideBudgetFromEmail(
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "approved", required = false) Boolean approved) {
        return decideBudget(token, approved);
    }

    private ResponseEntity<BudgetDecisionResponse> decideBudget(
            String approvalToken,
            Boolean approved) {
        if (approvalToken == null || approvalToken.isBlank() || approved == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Approval token and decision are required."
            );
        }

        applicationService.decideBudget(approvalToken, approved);
        String message = approved
                ? "Orçamento aprovado com sucesso."
                : "Orçamento rejeitado com sucesso.";
        return ResponseEntity.ok(new BudgetDecisionResponse(message));
    }
}
