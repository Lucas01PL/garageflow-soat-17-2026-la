package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.integration;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.model.Client;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.client.domain.repository.ClientRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.RepairOrder;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.repository.RepairOrderRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.model.Vehicle;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.vehicle.domain.repository.VehicleRepository;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.model.WorkshopService;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.domain.repository.WorkshopServiceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.mongodb.uri=mongodb://admin:admin123@localhost:27017/garageflow-soat-17-2026-integration-test?authSource=admin"
})
class RepairOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private WorkshopServiceRepository workshopServiceRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void cleanDatabase() {

        repairOrderRepository.findAll()
                .forEach(repairOrder ->
                        repairOrderRepository.deleteById(repairOrder.getId()));

        workshopServiceRepository.findAll()
                .forEach(service ->
                        workshopServiceRepository.deleteById(service.getId()));

        vehicleRepository.findAll()
                .forEach(vehicle ->
                        vehicleRepository.delete(vehicle.getId()));

        clientRepository.findAll()
                .forEach(client ->
                        clientRepository.delete(client.getId()));
    }

    @Test
    @WithMockUser(roles = "OPERATOR")
    void shouldCreateAndPersistRepairOrderThroughHttp() throws Exception {

        Client client = clientRepository.save(
                new Client(
                        "Integration Client",
                        "52998224725",
                        "85999990000",
                        "integration@test.com",
                        "Integration Street, 100"
                )
        );

        Vehicle vehicle = vehicleRepository.save(
                new Vehicle(
                        "INT1A23",
                        "Ford",
                        "Ka",
                        2018
                )
        );

        String response = mockMvc.perform(
                        post("/repairorder")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "customerId": "%s",
                                                  "vehicleId": "%s"
                                                }
                                                """.formatted(
                                                client.getId(),
                                                vehicle.getId()
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.number", notNullValue()))
                .andExpect(jsonPath("$.status", is("Recebida")))
                .andExpect(jsonPath("$.customer.customerId", is(client.getId())))
                .andExpect(jsonPath("$.vehicle.vehicleId", is(vehicle.getId())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String repairOrderId =
                com.jayway.jsonpath.JsonPath.read(response, "$.id");

        /*
         * Verifica que a informação realmente foi persistida
         * e não apenas retornada pelo controller.
         */
        RepairOrder persisted =
                repairOrderRepository.findById(repairOrderId)
                        .orElseThrow();

        assertThat(persisted.getId())
                .isEqualTo(repairOrderId);

        assertThat(persisted.getNumber())
                .isNotBlank();

        assertThat(persisted.getCustomer().getCustomerId())
                .isEqualTo(client.getId());

        assertThat(persisted.getVehicle().getVehicleId())
                .isEqualTo(vehicle.getId());

        /*
         * Consulta novamente através da API.
         */
        mockMvc.perform(
                        get("/repairorder/{id}", repairOrderId)
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(repairOrderId)))
                .andExpect(jsonPath("$.status", is("Recebida")));
    }

    @Test
    @WithMockUser(roles = "OPERATOR")
    void shouldExecuteCompleteRepairOrderFlowThroughHttp() throws Exception {

        Client client = clientRepository.save(
                new Client(
                        "Flow Client",
                        "39053344705",
                        "85988880000",
                        "flow@test.com",
                        "Flow Street, 200"
                )
        );

        Vehicle vehicle = vehicleRepository.save(
                new Vehicle(
                        "INT2B34",
                        "Ford",
                        "Ka",
                        2018
                )
        );

        WorkshopService service =
                workshopServiceRepository.save(
                        new WorkshopService(
                                "Troca de óleo",
                                new BigDecimal("150.00")
                        )
                );

        /*
         * 1. Criar OS
         */
        String createResponse =
                mockMvc.perform(
                                post("/repairorder")
                                        .header(
                                                HttpHeaders.AUTHORIZATION,
                                                "Bearer " + getAuthToken()
                                        )
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("""
                                                        {
                                                          "customerId": "%s",
                                                          "vehicleId": "%s"
                                                        }
                                                        """.formatted(
                                                        client.getId(),
                                                        vehicle.getId()
                                                )
                                        )
                        )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.status", is("Recebida")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        String repairOrderId =
                com.jayway.jsonpath.JsonPath.read(
                        createResponse,
                        "$.id"
                );

        /*
         * 2. Iniciar diagnóstico
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/in-diagnosis",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Em Diagnóstico")));

        /*
         * 3. Adicionar serviço
         */
        mockMvc.perform(
                        post(
                                "/repairorder/{id}/services",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "workshopServiceId": "%s",
                                          "quantity": 1
                                        }
                                        """.formatted(service.getId())
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Em Diagnóstico")))
                .andExpect(jsonPath(
                        "$.workshopServices[0].workshopServiceId",
                        is(service.getId())
                ))
                .andExpect(jsonPath(
                        "$.workshopServices[0].quantity",
                        is(1)
                ))
                .andExpect(jsonPath(
                        "$.totalServices",
                        is(150.0)
                ))
                .andExpect(jsonPath(
                        "$.total",
                        is(150.0)
                ));

        /*
         * 4. Solicitar aprovação
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/awaiting-approval",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Aguardando aprovação")
                ));

        /*
         * 5. Cliente aprova
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/approved",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Aprovado")
                ));

        /*
         * 6. Iniciar execução
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/in-execution",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Em Execução")
                ));

        /*
         * 7. Iniciar serviço
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/services/{serviceId}/status/start",
                                repairOrderId,
                                service.getId()
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Em Execução")
                ))
                .andExpect(jsonPath(
                        "$.workshopServices[0].status",
                        is("Em Execução")
                ));

        /*
         * 8. Finalizar serviço
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/services/{serviceId}/status/finished",
                                repairOrderId,
                                service.getId()
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "durationInMinutes": 45
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Finalizada")
                ))
                .andExpect(jsonPath(
                        "$.workshopServices[0].status",
                        is("Finalizado")
                ))
                .andExpect(jsonPath(
                        "$.workshopServices[0].durationInMinutes",
                        is(45)
                ));

        /*
         * 9. Entregar veículo
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/deliver",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Entregue")
                ))
                .andExpect(jsonPath(
                        "$.finishDate",
                        notNullValue()
                ));

        /*
         * 10. Verificar persistência final
         */
        RepairOrder persisted =
                repairOrderRepository.findById(repairOrderId)
                        .orElseThrow();

        assertThat(persisted.getStatus().name())
                .isEqualTo("DELIVERED");

        assertThat(persisted.getWorkshopServices())
                .hasSize(1);

        assertThat(
                persisted.getWorkshopServices()
                        .getFirst()
                        .getDurationInMinutes()
        ).isEqualTo(45);
    }

    @Test
    @WithMockUser(roles = "OPERATOR")
    void shouldKeepRejectedRepairOrderAsTerminalState() throws Exception {

        Client client = clientRepository.save(
                new Client(
                        "Rejected Client",
                        "11144477735",
                        "85977770000",
                        "rejected@test.com",
                        "Rejected Street, 300"
                )
        );

        Vehicle vehicle = vehicleRepository.save(
                new Vehicle(
                        "INT3C45",
                        "Ford",
                        "Ka",
                        2018
                )
        );

        WorkshopService service =
                workshopServiceRepository.save(
                        new WorkshopService(
                                "Diagnóstico",
                                new BigDecimal("100.00")
                        )
                );

        String response =
                mockMvc.perform(
                                post("/repairorder")
                                        .header(
                                                HttpHeaders.AUTHORIZATION,
                                                "Bearer " + getAuthToken()
                                        )
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("""
                                                        {
                                                          "customerId": "%s",
                                                          "vehicleId": "%s"
                                                        }
                                                        """.formatted(
                                                        client.getId(),
                                                        vehicle.getId()
                                                )
                                        )
                        )
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        String repairOrderId =
                com.jayway.jsonpath.JsonPath.read(
                        response,
                        "$.id"
                );

        /*
         * RECEIVED → IN_DIAGNOSIS
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/in-diagnosis",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk());

        /*
         * Adiciona serviço.
         */
        mockMvc.perform(
                        post(
                                "/repairorder/{id}/services",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "workshopServiceId": "%s",
                                          "quantity": 1
                                        }
                                        """.formatted(service.getId()))
                )
                .andExpect(status().isOk());

        /*
         * IN_DIAGNOSIS → AWAITING_APPROVAL
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/awaiting-approval",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk());

        /*
         * AWAITING_APPROVAL → REJECTED
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/rejected",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("Rejeitado")
                ));

        /*
         * REJECTED → APPROVED
         * Deve ser proibido.
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/approved",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isBadRequest());

        /*
         * REJECTED → IN_DIAGNOSIS
         * Deve ser proibido.
         */
        mockMvc.perform(
                        patch(
                                "/repairorder/{id}/status/in-diagnosis",
                                repairOrderId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + getAuthToken()
                                )
                )
                .andExpect(status().isBadRequest());

        /*
         * Confirma que o estado persistido continua REJECTED.
         */
        RepairOrder persisted =
                repairOrderRepository.findById(repairOrderId)
                        .orElseThrow();

        assertThat(persisted.getStatus().name())
                .isEqualTo("REJECTED");
    }

    private String getAuthToken() throws Exception {

        String response = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "admin@garageflow.com",
                                          "password": "admin123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json.get("token").asText();
    }
}