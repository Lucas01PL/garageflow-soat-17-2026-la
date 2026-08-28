package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.application.usecase.*;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseList;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.model.PurchaseListStatus;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.dto.PurchaseListResponse;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.presentation.mapper.PurchaseListMapper;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseListControllerTest {

    @Mock
    private GeneratePurchaseListUseCase generatePurchaseListUseCase;

    @Mock
    private ListPurchaseListsUseCase listPurchaseListsUseCase;

    @Mock
    private GetPurchaseListByIdUseCase getPurchaseListByIdUseCase;

    @Mock
    private ApprovePurchaseListUseCase approvePurchaseListUseCase;

    @Mock
    private RejectPurchaseListUseCase rejectPurchaseListUseCase;

    @Mock
    private PurchasePurchaseListUseCase purchasePurchaseListUseCase;

    @Mock
    private PurchaseListMapper mapper;

    @InjectMocks
    private PurchaseListController controller;

    @org.junit.jupiter.api.BeforeEach
    void setUpAuth() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new AuthenticatedUser("admin-1", "admin@example.com"),
                        null,
                        List.of()
                )
        );
    }

    @Test
    void shouldGeneratePurchaseList() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        PurchaseListResponse response = new PurchaseListResponse("pl-1", null, PurchaseListStatus.PENDING, List.of(), null, null);

        when(generatePurchaseListUseCase.generate(10)).thenReturn(purchaseList);
        when(mapper.toResponse(purchaseList)).thenReturn(response);

        ResponseEntity<PurchaseListResponse> result = controller.generate(10);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldListPurchaseLists() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        PurchaseListResponse response = new PurchaseListResponse("pl-1", null, PurchaseListStatus.PENDING, List.of(), null, null);

        when(listPurchaseListsUseCase.execute(PurchaseListStatus.PENDING)).thenReturn(List.of(purchaseList));
        when(mapper.toResponse(purchaseList)).thenReturn(response);

        ResponseEntity<List<PurchaseListResponse>> result = controller.list(PurchaseListStatus.PENDING);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void shouldGetPurchaseListById() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PENDING).build();
        PurchaseListResponse response = new PurchaseListResponse("pl-1", null, PurchaseListStatus.PENDING, List.of(), null, null);

        when(getPurchaseListByIdUseCase.execute("pl-1")).thenReturn(purchaseList);
        when(mapper.toResponse(purchaseList)).thenReturn(response);

        ResponseEntity<PurchaseListResponse> result = controller.getById("pl-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldApprovePurchaseList() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.APPROVED).build();
        PurchaseListResponse response = new PurchaseListResponse("pl-1", null, PurchaseListStatus.APPROVED, List.of(), "admin-1", null);

        when(approvePurchaseListUseCase.execute("pl-1", "admin-1")).thenReturn(purchaseList);
        when(mapper.toResponse(purchaseList)).thenReturn(response);

        ResponseEntity<PurchaseListResponse> result = controller.approve("pl-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldRejectPurchaseList() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.REJECTED).build();
        PurchaseListResponse response = new PurchaseListResponse("pl-1", null, PurchaseListStatus.REJECTED, List.of(), "admin-1", null);

        when(rejectPurchaseListUseCase.execute("pl-1", "admin-1")).thenReturn(purchaseList);
        when(mapper.toResponse(purchaseList)).thenReturn(response);

        ResponseEntity<PurchaseListResponse> result = controller.reject("pl-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldPurchasePurchaseList() {
        PurchaseList purchaseList = PurchaseList.builder().id("pl-1").status(PurchaseListStatus.PURCHASED).build();
        PurchaseListResponse response = new PurchaseListResponse("pl-1", null, PurchaseListStatus.PURCHASED, List.of(), "admin-1", null);

        when(purchasePurchaseListUseCase.execute("pl-1")).thenReturn(purchaseList);
        when(mapper.toResponse(purchaseList)).thenReturn(response);

        ResponseEntity<PurchaseListResponse> result = controller.purchase("pl-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }
}
