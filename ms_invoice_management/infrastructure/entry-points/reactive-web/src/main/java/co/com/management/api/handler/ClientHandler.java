package co.com.management.api.handler;

import co.com.management.api.Constants;
import co.com.management.api.RequestValidator;
import co.com.management.api.dto.mapper.RQMapper;
import co.com.management.api.dto.mapper.RSMapper;
import co.com.management.api.dto.request.ClientDTO;
import co.com.management.api.dto.response.InvoiceResponseDTO;
import co.com.management.api.dto.response.StructResponse;
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


    private static final Set<String> INFODOC_PARAMS = Set.of(Constants.NUM_PARAM, Constants.TYPE_PARAM);

    private final RequestValidator validator;
    private final ClientUseCase clientUseCase;
    private final RQMapper rqMapper;
    private final RSMapper rsMapper;


    public Mono<ServerResponse> saveClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientDTO.class)
                .flatMap(validator::validateDto)
                .map(rqMapper::toModel)
                .flatMap(clientUseCase::saveClient)
                .map(rsMapper::response)
                .flatMap(clientSaved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(StructResponse.structureRS(clientSaved, HttpStatus.OK.value())));
    }

    public Mono<ServerResponse> updateClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientDTO.class)
                .flatMap(dto -> validator.validateDto(dto, ClientDTO.Update.class))
                .map(rqMapper::toModel)
                .flatMap(clientUseCase::updateClient)
                .map(rsMapper::response)
                .flatMap(clientUpdated -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(StructResponse.structureRS(clientUpdated, HttpStatus.OK.value())));
    }


    public Mono<ServerResponse> getClientsPageable(ServerRequest request) {
        return validator.requireParams(request, Constants.PAGINATION_PARAMS)
                .flatMap(req -> {
                    int page = Integer.parseInt(req.queryParam(Constants.PAGE_PARAM).orElse("1"));
                    int size = Integer.parseInt(req.queryParam(Constants.SIZE_PARAM).orElse("10"));

                    return clientUseCase.getAllPageable(page, size)
                            .map(rsMapper::toPageResultClientDTO)
                            .flatMap(dto -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(StructResponse.structureRS(dto, HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> deleteClient(ServerRequest request) {
        return validator.requirePathVariables(request, Set.of(Constants.ID_PATH))
                .flatMap(req -> {
                    UUID id = UUID.fromString(req.pathVariable(Constants.ID_PATH));
                    return clientUseCase.deleteById(id)
                            .then(ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS("ELIMINÉ", HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> getClientById(ServerRequest request) {
        return validator.requirePathVariables(request, Set.of(Constants.ID_PATH))
                .flatMap(req -> {
                    UUID id = UUID.fromString(req.pathVariable(Constants.ID_PATH));
                    return clientUseCase.getById(id)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS(dto, HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> getClientByInfoDoc(ServerRequest request) {
        return validator.requireParams(request, INFODOC_PARAMS)
                .flatMap(req -> {
                    String num = req.queryParam(Constants.NUM_PARAM).get();
                    String type = req.queryParam(Constants.TYPE_PARAM).get();

                    return clientUseCase.findByInfoDocument(num, type)
                            .map(rsMapper::response)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS(dto, HttpStatus.OK.value())));
                });
    }
}
