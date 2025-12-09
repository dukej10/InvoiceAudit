package co.com.management.api;

import co.com.management.api.exception.MissingParametersException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public Mono<ServerRequest> requireParams(
            ServerRequest request,
            Collection<String> requiredParams) {

        Set<String> missing = requiredParams.stream()
                .filter(p -> request.queryParam(p).isEmpty())
                .collect(Collectors.toSet());

        return missing.isEmpty()
                ? Mono.just(request)
                : Mono.error(new MissingParametersException(missing));
    }

    public Mono<ServerRequest> requirePathVariables(
            ServerRequest request,
            Collection<String> requiredPathVars) {

        Set<String> missing = requiredPathVars.stream()
                .filter(v -> {
                    try {
                        request.pathVariable(v);
                        return false; // existe
                    } catch (IllegalArgumentException ex) {
                        return true; // no existe
                    }
                })
                .collect(Collectors.toSet());

        return missing.isEmpty()
                ? Mono.just(request)
                : Mono.error(new MissingParametersException(missing));
    }

    public Mono<ServerRequest> requireParamsAndPathVars(
            ServerRequest request,
            Collection<String> requiredParams,
            Collection<String> requiredPathVars) {

        return requireParams(request, requiredParams)
                .flatMap(req -> requirePathVariables(req, requiredPathVars));
    }

    public <T> Mono<T> validateDto(T dto, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto, groups);
        return violations.isEmpty()
                ? Mono.just(dto)
                : Mono.error(new ConstraintViolationException("Error de validación en los campos", violations));
    }

    // Versión sin grupos (útil para creación cuando no tienes grupo Create)
    public <T> Mono<T> validateDto(T dto) {
        return validateDto(dto, new Class<?>[0]);
    }
}