package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.presentation.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.DuplicatePartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.PartNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class PartExceptionHandler {

    @ExceptionHandler(PartNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(PartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.NOT_FOUND.value(),
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(DuplicatePartException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicatePartException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.CONFLICT.value(),
                "message", ex.getMessage()
        ));
    }
}
