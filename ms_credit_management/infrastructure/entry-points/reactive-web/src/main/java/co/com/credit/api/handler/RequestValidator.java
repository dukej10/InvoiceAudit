package co.com.credit.api.handler;

import co.com.credit.api.exception.MissingParametersException;
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