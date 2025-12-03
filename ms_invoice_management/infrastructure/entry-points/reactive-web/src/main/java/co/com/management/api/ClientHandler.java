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
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ClientHandler {

    private final RequestValidator validator;

    private static final Set<String> PAGINATION_PARAMS = Set.of("page", "size");
    private static final Set<String> FILTER_PARAMS = Set.of("status", "from", "to");

    private final ClientUseCase clientUseCase;

    public Mono<ServerResponse> saveClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientDTO.class)
                .map(RequestMapper::toModel)
                .flatMap(clientUseCase::saveClient)
                .flatMap(clientSaved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Utility.structureRS(clientSaved, HttpStatus.OK.value())));
    }

    public Mono<ServerResponse> updateClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientDTO.class)
                .map(RequestMapper::toModel)
                .flatMap(clientUseCase::updateClient)
                .flatMap(clientUpdated -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Utility.structureRS(clientUpdated, HttpStatus.OK.value())));
    }


    public Mono<ServerResponse> getClientsPageable(ServerRequest request) {
        return validator.requireParams(request, PAGINATION_PARAMS)
                .flatMap(req -> {
                    int page = Integer.parseInt(req.queryParam("page").get());
                    int size = Integer.parseInt(req.queryParam("size").orElse("10"));

                    return clientUseCase.getAllPageable(page, size)
                            .map(ResponseMapper::toPageResultClientDTO)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(Utility.structureRS(dto, HttpStatus.OK.value())));
                });
    }
}
