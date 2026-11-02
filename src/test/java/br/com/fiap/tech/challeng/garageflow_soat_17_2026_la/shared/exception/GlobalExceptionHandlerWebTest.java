package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Garante que os erros do cliente (4xx) gerados pelo Spring nao sejam convertidos em 500
 * pelo handler generico, e que erros realmente inesperados continuem sendo 500.
 */
class GlobalExceptionHandlerWebTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn400WhenRequestBodyIsMalformedJson() throws Exception {
        mockMvc.perform(post("/dummy/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/dummy/json"));
    }

    @Test
    void shouldReturn415WhenContentTypeIsNotSupported() throws Exception {
        mockMvc.perform(post("/dummy/json")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("x"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.path").value("/dummy/json"));
    }

    @Test
    void shouldReturn400WhenRequiredParameterIsMissing() throws Exception {
        mockMvc.perform(post("/dummy/param")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/dummy/param"));
    }

    @Test
    void shouldReturn405WhenMethodIsNotAllowed() throws Exception {
        mockMvc.perform(get("/dummy/json"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));
    }

    @Test
    void shouldPreserveStatusAndReasonOfResponseStatusException() throws Exception {
        mockMvc.perform(get("/dummy/status"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Parametros obrigatorios ausentes."))
                .andExpect(jsonPath("$.path").value("/dummy/status"));
    }

    @Test
    void shouldReturn500WithGenericMessageForUnexpectedErrors() throws Exception {
        mockMvc.perform(get("/dummy/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno no servidor."))
                .andExpect(jsonPath("$.path").value("/dummy/boom"));
    }

    @RestController
    static class DummyController {

        @PostMapping(value = "/dummy/json", consumes = MediaType.APPLICATION_JSON_VALUE)
        Map<String, String> json(@RequestBody Map<String, String> body) {
            return body;
        }

        @PostMapping("/dummy/param")
        String param(@RequestParam("name") String name) {
            return name;
        }

        @GetMapping("/dummy/status")
        String status() {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametros obrigatorios ausentes.");
        }

        @GetMapping("/dummy/boom")
        String boom() {
            throw new IllegalStateException("falha inesperada");
        }
    }
}
