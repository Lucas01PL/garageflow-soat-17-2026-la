package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderApplicationService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RepairOrderExternalControllerTest {

    @Mock
    private RepairOrderApplicationService applicationService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new RepairOrderExternalController(
                                applicationService
                        )
                )
                .build();
    }

    @Test
    void shouldDecideBudgetFromEmailFormPost() throws Exception {
        RepairOrder repairOrder = RepairOrder.builder().id("repair-order-1").build();
        when(applicationService.decideBudget("email-token", true))
                .thenReturn(repairOrder);

        mockMvc.perform(post("/external/repair-orders/budget-decision")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "email-token")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Orçamento aprovado com sucesso."))
                .andExpect(jsonPath("$.size()").value(1));

        verify(applicationService).decideBudget("email-token", true);
    }

    @Test
    void shouldRejectBudgetFromEmailFormPost() throws Exception {
        RepairOrder repairOrder = RepairOrder.builder().id("repair-order-1").build();
        when(applicationService.decideBudget("email-token", false))
                .thenReturn(repairOrder);

        mockMvc.perform(post("/external/repair-orders/budget-decision")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "email-token")
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Orçamento rejeitado com sucesso."));

        verify(applicationService).decideBudget("email-token", false);
    }

    @Test
    void shouldKeepBadRequestBehaviorWhenDecisionFieldsAreMissing() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new RepairOrderExternalController(applicationService)
                        .decideBudgetFromEmail("", true)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

}
