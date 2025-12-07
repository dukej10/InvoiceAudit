package co.com.management.api.handler;

import co.com.management.api.RequestValidator;
import co.com.management.api.Utility;
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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientHandler {

    private final RequestValidator validator;

    private static final Set<String> PAGINATION_PARAMS = Set.of("page", "size");
    private static final Set<String> FILTER_PARAMS = Set.of("status", "from", "to");
    private static final Set<String> INFODOC_PARAMS = Set.of("num", "type");

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
                .map(ResponseMapper::responseFull)
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

    public Mono<ServerResponse> deleteClient(ServerRequest request) {
        return validator.requirePathVariables(request, Set.of("id"))
                .flatMap(req -> {
                    UUID id = UUID.fromString(req.pathVariable("id"));
                    return clientUseCase.deleteById(id)
                            .then(ServerResponse.ok()
                                    .bodyValue(Utility.structureRS("ELIMINÉ", HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> getClientById(ServerRequest request) {
        return validator.requirePathVariables(request, Set.of("id"))
                .flatMap(req -> {
                    UUID id = UUID.fromString(req.pathVariable("id"));
                    return clientUseCase.getById(id)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(Utility.structureRS(dto, HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> getClientByInfoDoc(ServerRequest request) {
        return validator.requireParams(request, INFODOC_PARAMS)
                .flatMap(req -> {
                    String num = req.queryParam("num").get();
                    String type = req.queryParam("type").get();

                    return clientUseCase.findByInfoDocument(num, type)
                            .map(ResponseMapper::responseFull)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(Utility.structureRS(dto, HttpStatus.OK.value())));
                });
    }
}
