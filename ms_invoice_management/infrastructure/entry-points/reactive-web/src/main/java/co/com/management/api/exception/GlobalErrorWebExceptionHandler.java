package co.com.management.api.exception;

import co.com.management.api.Utility;
import co.com.management.model.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> exceptionHandlers;


    public GlobalErrorWebExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties webProperties,
            ApplicationContext applicationContext,
            ServerCodecConfigurer codecConfigurer) {

        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
        this.setMessageReaders(codecConfigurer.getReaders());

        this.exceptionHandlers = Map.of(
                MissingParametersException.class,      this::handleMissingParams,
                ConstraintViolationException.class,    this::handleConstraintViolation,
                BusinessException.class, this::handleSpecific,
                GeneralSecurityException.class, this::handleAuthorize
        );
    }

    // Enrutamiento para todas las excepciones.
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    // 4. Mapear excepción de Mono<ServerResponse>
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        // Obtener la excepción del flujo
        Throwable error = getError(request);

        return exceptionHandlers.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(error))
                .findFirst()
                .map(entry -> entry.getValue().apply(error))
                .orElseGet(() -> handleGenericException(error));
    }

    private Mono<ServerResponse> handleMissingParams(Throwable t) {
        MissingParametersException ex = (MissingParametersException) t; // Cast seguro

        List<Map<String, String>> errors = ex.getMissingParameters().stream()
                .map(param -> Map.of(
                        "param", param,
                        "error", "es obligatorio"
                ))
                .toList();

        Map<String, Object> body = Map.of(
                "code", HttpStatus.BAD_REQUEST.value(),
                "message", "Error de validación: Parámetros faltantes",
                "errors", errors
        );

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Utility.structureRS(body, HttpStatus.BAD_REQUEST.value())));
    }

    private Mono<ServerResponse> handleConstraintViolation(Throwable t) {
        ConstraintViolationException ex = (ConstraintViolationException) t;

        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String field = violation.getPropertyPath().toString();
                    int lastDot = field.lastIndexOf('.');
                    if (lastDot != -1) {
                        field = field.substring(lastDot + 1);
                    }
                    return Map.of(
                            "field", field,
                            "error", violation.getMessage()
                    );
                })
                .toList();

        Map<String, Object> body = Map.of(
                "code", HttpStatus.BAD_REQUEST.value(),
                "message", "Error de validación: Restricciones de datos",
                "errors", errors
        );

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Utility.structureRS(body, HttpStatus.BAD_REQUEST.value())));
    }

    private Mono<ServerResponse> handleAuthorize(Throwable ex) {

        Map<String, Object> body = Map.of(
                "timestamp", new Date(),
                "code", HttpStatus.UNAUTHORIZED.value(),
                "message", "Token inválido o expirado",
                "error", Optional.ofNullable(ex.getMessage()).orElse("Error desconocido")
        );

        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Utility.structureRS(body, HttpStatus.UNAUTHORIZED.value())));
    }

    private Mono<ServerResponse> handleSpecific(Throwable ex) {

        Map<String, Object> body = Map.of(
                "timestamp", new Date(),
                "code", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "message", "Error interno del servidor",
                "error", Optional.ofNullable(ex.getMessage()).orElse("Error desconocido")
        );

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Utility.structureRS(body, HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }

    private Mono<ServerResponse> handleGenericException(Throwable ex) {

        Map<String, Object> body = Map.of(
                "code", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "message", "Error interno del servidor",
                "error", Optional.ofNullable(ex.getMessage()).orElse("Error desconocido")
        );

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Utility.structureRS(body, HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }
}