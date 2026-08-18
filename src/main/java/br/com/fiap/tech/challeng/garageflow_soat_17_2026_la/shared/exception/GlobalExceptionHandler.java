package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception.InvalidPurchaseListStatusException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(NotEnoughResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleNotEnoughResource(
            NotEnoughResourceException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                .body(new ApiErrorResponse(
                        HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),
                        HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.getReasonPhrase(),
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI()
                ));
    }
  
    @ExceptionHandler(InvalidPurchaseListStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPurchaseListStatus(
            InvalidPurchaseListStatusException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDocument(
            InvalidDocumentException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI()
                ));
    }
}
