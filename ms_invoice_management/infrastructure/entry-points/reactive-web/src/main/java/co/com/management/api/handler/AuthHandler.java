package co.com.management.api.handler;

import co.com.management.api.RequestValidator;
import co.com.management.api.Utility;
import co.com.management.api.dto.mapper.ResponseMapper;
import co.com.management.api.dto.request.LoginDTO;
import co.com.management.security.JwtProvider;
import co.com.management.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final LoginUseCase loginUseCase;
    private final JwtProvider jwtProvider;
    private final RequestValidator validator;


    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
                .flatMap(validator::validateDto)
                .flatMap(r -> loginUseCase.login(r.getUsername(), r.getPassword()))
                .map(u -> jwtProvider.generate(u.getUsername(), u.getRoles()))
                .map(ResponseMapper::toResponse)
                .flatMap(clientSaved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Utility.structureRS(clientSaved, HttpStatus.OK.value())));    }
}
