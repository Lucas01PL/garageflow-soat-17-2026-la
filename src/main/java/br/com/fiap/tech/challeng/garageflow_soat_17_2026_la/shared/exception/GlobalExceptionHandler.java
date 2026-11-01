package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.purchasing.domain.exception.InvalidPurchaseListStatusException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.domain.exception.DuplicatePartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartStockOperationException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.WorkshopServiceNotFoundException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderStateException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidRepairOrderItemException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InvalidPartException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.InsufficientQuantityException;

@Slf4j
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

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
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

    @ExceptionHandler({RequiredObjectException.class, RequiredFieldException.class, InvalidFieldValueException.class})
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            BusinessException ex,
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

    @ExceptionHandler(DuplicatePartException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicatePart(
            DuplicatePartException ex,
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

    @ExceptionHandler({PartNotFoundException.class, WorkshopServiceNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleDomainNotFound(
            BusinessException ex,
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

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientQuantity(
            InsufficientQuantityException ex,
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

    @ExceptionHandler(PartStockOperationException.class)
    public ResponseEntity<ApiErrorResponse> handlePartStockOperation(
            PartStockOperationException ex,
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

    @ExceptionHandler({InvalidRepairOrderStateException.class, InvalidRepairOrderItemException.class, InvalidPartException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidRepairOrderExceptions(
            BusinessException ex,
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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex,
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage(),
                        Instant.now(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
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

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationCredentialsNotFound(
            AuthenticationCredentialsNotFoundException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Usuário não autenticado. Por favor, realize o login.",
                Instant.now(),
                ""
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJwtException(
            MalformedJwtException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Token inválido: formato incorreto.",
                Instant.now(),
                ""
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Corpo da requisição ausente ou malformado.",
                Instant.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatus status = HttpStatus.resolve(errorResponse.getStatusCode().value());
            String reason = status != null ? status.getReasonPhrase() : "Error";
            String detail = errorResponse.getBody().getDetail();

            ApiErrorResponse error = new ApiErrorResponse(
                    errorResponse.getStatusCode().value(),
                    reason,
                    detail != null ? detail : reason,
                    Instant.now(),
                    request.getRequestURI()
            );

            return ResponseEntity.status(errorResponse.getStatusCode()).body(error);
        }

        log.error("Erro inesperado em {}", request.getRequestURI(), ex);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocorreu um erro interno no servidor.",
                Instant.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
