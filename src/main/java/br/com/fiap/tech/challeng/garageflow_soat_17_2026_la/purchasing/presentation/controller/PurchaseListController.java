package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto.PurchaseListDecisionRequest;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto.PurchaseListResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.mapper.PurchaseListMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.presentation.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-lists")
public class PurchaseListController extends BaseController {

    private final GeneratePurchaseListUseCase generatePurchaseListUseCase;
    private final ListPurchaseListsUseCase listPurchaseListsUseCase;
    private final GetPurchaseListByIdUseCase getPurchaseListByIdUseCase;
    private final ApprovePurchaseListUseCase approvePurchaseListUseCase;
    private final RejectPurchaseListUseCase rejectPurchaseListUseCase;
    private final PurchasePurchaseListUseCase purchasePurchaseListUseCase;
    private final PurchaseListMapper mapper;

    public PurchaseListController(GeneratePurchaseListUseCase generatePurchaseListUseCase,
                                   ListPurchaseListsUseCase listPurchaseListsUseCase,
                                   GetPurchaseListByIdUseCase getPurchaseListByIdUseCase,
                                   ApprovePurchaseListUseCase approvePurchaseListUseCase,
                                   RejectPurchaseListUseCase rejectPurchaseListUseCase,
                                   PurchasePurchaseListUseCase purchasePurchaseListUseCase,
                                   PurchaseListMapper mapper) {
        this.generatePurchaseListUseCase = generatePurchaseListUseCase;
        this.listPurchaseListsUseCase = listPurchaseListsUseCase;
        this.getPurchaseListByIdUseCase = getPurchaseListByIdUseCase;
        this.approvePurchaseListUseCase = approvePurchaseListUseCase;
        this.rejectPurchaseListUseCase = rejectPurchaseListUseCase;
        this.purchasePurchaseListUseCase = purchasePurchaseListUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Generate Purchase List",
            description = "Generates a new pending purchase list from parts currently at or below the low-stock threshold."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Purchase list generated")
    })
    @PostMapping
    public ResponseEntity<PurchaseListResponse> generate(@RequestParam(required = false) Integer threshold) {
        PurchaseList purchaseList = generatePurchaseListUseCase.generate(threshold);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(purchaseList));
    }

    @Operation(
            summary = "List Purchase Lists",
            description = "Retrieves all purchase lists, optionally filtered by status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase lists listed")
    })
    @GetMapping
    public ResponseEntity<List<PurchaseListResponse>> list(@RequestParam(required = false) PurchaseListStatus status) {
        List<PurchaseListResponse> response = listPurchaseListsUseCase.execute(status).stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Get Purchase List",
            description = "Retrieves a purchase list by its id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase list found"),
            @ApiResponse(responseCode = "404", description = "Purchase list not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseListResponse> getById(@PathVariable String id) {
        PurchaseList purchaseList = getPurchaseListByIdUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(purchaseList));
    }

    @Operation(
            summary = "Approve Purchase List",
            description = "Approves a pending purchase list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase list approved"),
            @ApiResponse(responseCode = "404", description = "Purchase list not found"),
            @ApiResponse(responseCode = "409", description = "Purchase list is not pending")
    })
    @PatchMapping("/{id}/approve")
    public ResponseEntity<PurchaseListResponse> approve(@PathVariable String id) {
        String userId = resolveCurrentUserId();
        PurchaseList purchaseList = approvePurchaseListUseCase.execute(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(purchaseList));
    }

    @Operation(
            summary = "Reject Purchase List",
            description = "Rejects a pending purchase list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase list rejected"),
            @ApiResponse(responseCode = "404", description = "Purchase list not found"),
            @ApiResponse(responseCode = "409", description = "Purchase list is not pending")
    })
    @PatchMapping("/{id}/reject")
    public ResponseEntity<PurchaseListResponse> reject(@PathVariable String id) {
        String userId = resolveCurrentUserId();
        PurchaseList purchaseList = rejectPurchaseListUseCase.execute(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(purchaseList));
    }

    @Operation(
            summary = "Purchase Purchase List",
            description = "Marks an approved purchase list as purchased and restocks each part accordingly."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase list purchased and stock replenished"),
            @ApiResponse(responseCode = "404", description = "Purchase list not found"),
            @ApiResponse(responseCode = "409", description = "Purchase list is not approved")
    })
    @PostMapping("/{id}/purchase")
    public ResponseEntity<PurchaseListResponse> purchase(@PathVariable String id) {
        PurchaseList purchaseList = purchasePurchaseListUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(purchaseList));
    }
}
