package co.com.management.api;

import co.com.management.api.exception.MissingParametersException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequestValidator {

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
}