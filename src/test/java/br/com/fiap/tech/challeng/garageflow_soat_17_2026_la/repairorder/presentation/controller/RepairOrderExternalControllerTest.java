package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service.RepairOrderApplicationService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.dto.response.RepairOrderResponseDTO;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.presentation.mapper.RepairOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RepairOrderExternalControllerTest {

    @Mock
    private RepairOrderApplicationService applicationService;

    @Mock
    private RepairOrderMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new RepairOrderExternalController(
                                applicationService,
                                mapper
                        )
                )
                .build();
    }

    @Test
    void shouldDecideBudgetFromEmailFormPost() throws Exception {
        RepairOrder repairOrder = RepairOrder.builder().id("repair-order-1").build();
        when(applicationService.decideBudget("email-token", true))
                .thenReturn(repairOrder);
        when(mapper.toResponse(repairOrder))
                .thenReturn(new RepairOrderResponseDTO());

        mockMvc.perform(post("/external/repair-orders/budget-decision")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "email-token")
                        .param("approved", "true"))
                .andExpect(status().isOk());

        verify(applicationService).decideBudget("email-token", true);
    }

    @Test
    void shouldRejectBudgetFromEmailFormPost() throws Exception {
        RepairOrder repairOrder = RepairOrder.builder().id("repair-order-1").build();
        when(applicationService.decideBudget("email-token", false))
                .thenReturn(repairOrder);
        when(mapper.toResponse(repairOrder))
                .thenReturn(new RepairOrderResponseDTO());

        mockMvc.perform(post("/external/repair-orders/budget-decision")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "email-token")
                        .param("approved", "false"))
                .andExpect(status().isOk());

        verify(applicationService).decideBudget("email-token", false);
    }

    @Test
    void shouldPreserveJsonAndApiKeyBudgetDecision() throws Exception {
        RepairOrder repairOrder = RepairOrder.builder().id("repair-order-1").build();
        when(applicationService.decideBudget("api-token", false))
                .thenReturn(repairOrder);
        when(mapper.toResponse(repairOrder))
                .thenReturn(new RepairOrderResponseDTO());

        mockMvc.perform(post("/external/repair-orders/budget-decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-Key", "api-token")
                        .content("{\"approved\":false}"))
                .andExpect(status().isOk());

        verify(applicationService).decideBudget("api-token", false);
    }
}
