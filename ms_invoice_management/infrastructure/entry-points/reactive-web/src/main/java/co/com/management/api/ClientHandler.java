package co.com.management.api;

import co.com.management.api.dto.mapper.RequestMapper;
import co.com.management.api.dto.mapper.ResponseMapper;
import co.com.management.api.dto.request.ClientDTO;
import co.com.management.usecase.client.ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ClientUseCase clientUseCase;

    public Mono<ServerResponse> saveClient(ServerRequest serverRequest) {
        return  serverRequest.bodyToMono(ClientDTO.class)
                .map(RequestMapper::toModel)
                .flatMap(clientUseCase::saveClient)
                .flatMap(clientSaved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue( Utility.structureRS(clientSaved, HttpStatus.OK.value())));
    }

    public Mono<ServerResponse> getClientsPaginable(ServerRequest request) {
        return request.queryParams()
                .toSingleValueMap()
                .containsKey("page") && request.queryParam("size").isPresent()
                ? processValidRequest(request)
                : ServerResponse.badRequest().bodyValue("Faltan parámetros page y/o size");
    }

    private Mono<ServerResponse> processValidRequest(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").get());
        int size = Integer.parseInt(request.queryParam("size").get());

        return clientUseCase.getAllPageable(page, size)
                .map(ResponseMapper::toPageResultClientDTO)
                .flatMap(dto -> ServerResponse.ok().bodyValue(
                        Utility.structureRS(dto, HttpStatus.OK.value())
                ));
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
