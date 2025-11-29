package co.com.credit.api.exception;

import co.com.credit.api.Utility;
import co.com.credit.api.dto.ResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Maneja errores de validación en el body (DTO con @Valid/@Validated)
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleValidationErrors(
            WebExchangeBindException ex, ServerWebExchange exchange) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(
                        fe.getField(),
                        fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value"))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                "Validation failed",
                exchange.getRequest().getPath().value(),
                fieldErrors
        );

        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Utility.structureRS(errorResponse, HttpStatus.BAD_REQUEST.value()))
        );
    }

    /**
     * Maneja errores de validación en path variables o query params (@Validated)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleConstraintViolation(
            ConstraintViolationException ex, ServerWebExchange exchange) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(v -> new ErrorResponse.FieldError(
                        v.getPropertyPath().toString(),
                        v.getMessage()))
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                "Validation failed",
                exchange.getRequest().getPath().value(),
                fieldErrors
        );

        return Mono.just(
                ResponseEntity.badRequest()
                        .body(Utility.structureRS(errorResponse, HttpStatus.BAD_REQUEST.value()))
        );
    }

    /**
     * Cualquier excepción no controlada
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleGlobalException(
            Exception ex, ServerWebExchange exchange) {

        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                ex.getMessage() != null ? ex.getMessage() : "Internal server error",
                exchange.getRequest().getPath().value(),
                List.of(new ErrorResponse.FieldError("exception", ex.getClass().getSimpleName()))
        );

        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Utility.structureRS(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR.value()))
        );
    }
}
