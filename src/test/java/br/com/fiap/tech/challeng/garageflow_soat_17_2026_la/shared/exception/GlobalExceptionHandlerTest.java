package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleInvalidDocumentException() {
        when(request.getRequestURI()).thenReturn("/client");
        InvalidDocumentException exception = new InvalidDocumentException("111.111.111-11");

        ResponseEntity<ApiErrorResponse> response = handler.handleInvalidDocument(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals("/client", response.getBody().path());
        assertEquals(exception.getMessage(), response.getBody().message());
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        when(request.getRequestURI()).thenReturn("/client/id-1");
        ResourceNotFoundException exception = new ResourceNotFoundException("Client", "id", "id-1");

        ResponseEntity<ApiErrorResponse> response = handler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
    }

    @Test
    void shouldHandleDuplicateResourceException() {
        when(request.getRequestURI()).thenReturn("/client");
        DuplicateResourceException exception = new DuplicateResourceException("Client", "document", "52998224725");

        ResponseEntity<ApiErrorResponse> response = handler.handleDuplicateResource(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status());
    }
}
