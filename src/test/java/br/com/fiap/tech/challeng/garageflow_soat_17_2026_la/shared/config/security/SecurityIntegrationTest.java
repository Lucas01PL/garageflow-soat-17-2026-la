package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.config.MongoTestContainerConfiguration;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.workshopservice.presentation.dto.CreateWorkshopServiceRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"jwt.secret=01234567890123456789012345678901", "jwt.expiration-minutes=60"})
@Import(MongoTestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
class SecurityIntegrationTest {
    private static final String SECRET = "01234567890123456789012345678901";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void withoutToken_userEndpoint_shouldReturn401() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isForbidden());
    }

    @Test
    void invalidToken_userEndpoint_shouldReturn401() throws Exception {
        mockMvc.perform(get("/user").header("Authorization", "Bearer invalid.token.value")).andExpect(status().isForbidden());
    }

    @Test
    void expiredToken_userEndpoint_shouldReturn401() throws Exception {
        JwtService expiredJwtService = new JwtService(SECRET, -1);
        String expiredToken = expiredJwtService.generateToken("admin@example.com", "ADMIN");
        mockMvc.perform(get("/user").header("Authorization", "Bearer " + expiredToken)).andExpect(status().isForbidden());
    }

    @Test
    void operatorOnAdminEndpoint_shouldReturn403() throws Exception {
        String operatorToken = jwtService.generateToken("operator@example.com", "OPERATOR");
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("Test", BigDecimal.TEN);
        mockMvc.perform(post("/workshopservice").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)).header("Authorization", "Bearer " + operatorToken)).andExpect(status().isForbidden());
    }

    @Test
    void adminOnAdminEndpoint_shouldReturn201() throws Exception {
        String adminToken = jwtService.generateToken("admin@example.com", "ADMIN");
        CreateWorkshopServiceRequestDTO dto = new CreateWorkshopServiceRequestDTO("Test", BigDecimal.TEN);
        mockMvc.perform(post("/workshopservice").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)).header("Authorization", "Bearer " + adminToken)).andExpect(status().isCreated());
    }
}