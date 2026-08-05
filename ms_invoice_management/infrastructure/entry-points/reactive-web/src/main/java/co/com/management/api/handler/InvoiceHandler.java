package co.com.management.api.handler;

import co.com.management.api.Constants;
import co.com.management.api.RequestValidator;
import co.com.management.api.dto.mapper.RQMapper;
import co.com.management.api.dto.mapper.RSMapper;
import co.com.management.api.dto.request.InvoiceDTO;
import co.com.management.api.dto.response.InvoiceResponseDTO;
import co.com.management.api.dto.response.StructResponse;
import co.com.management.usecase.invoice.InvoiceUseCase;
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
public class InvoiceHandler {


    private static final String CLIENT_ID = "clientId";
    private final RequestValidator validator;

    private final InvoiceUseCase invoiceUseCase;
    private final RQMapper rqMapper;
    private final RSMapper rsMapper;


    public Mono<ServerResponse> saveInvoice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(InvoiceDTO.class)
                .flatMap(validator::validateDto)
                .map(rqMapper::toModel)
                .flatMap(invoiceUseCase::createInvoice)
                .map(rsMapper::response)
                .flatMap(invoiceSaved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(StructResponse.structureRS(invoiceSaved, HttpStatus.OK.value())));
    }

    public Mono<ServerResponse> getInvoicesByClientPageable(ServerRequest request) {
        return validator.requireParamsAndPathVars(request, Constants.PAGINATION_PARAMS, Set.of(CLIENT_ID))
                .flatMap(req -> {
                    int page = Integer.parseInt(req.queryParam(Constants.PAGE_PARAM).orElse("0"));
                    int size = Integer.parseInt(req.queryParam(Constants.SIZE_PARAM).orElse("10"));
                    UUID clientId = UUID.fromString(req.pathVariable(CLIENT_ID));

                    return invoiceUseCase.getAllByClientId(clientId, page, size)
                            .map(rsMapper::toPageResultInvoiceDTO)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS(dto, HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> getInvoicesPageable(ServerRequest request) {
        return validator.requirePathVariables(request, Constants.PAGINATION_PARAMS)
                .flatMap(req -> {
                    int page = Integer.parseInt(req.queryParam(Constants.PAGE_PARAM).orElse("0"));
                    int size = Integer.parseInt(req.queryParam(Constants.SIZE_PARAM).orElse("10"));

                    return invoiceUseCase.getAllPageable(page, size)
                            .map(rsMapper::toPageResultInvoiceDTO)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS(dto, HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> deleteInvoice(ServerRequest request) {
        return validator.requirePathVariables(request, Set.of(Constants.ID_PATH))
                .flatMap(req -> {
                    UUID id = UUID.fromString(req.pathVariable(Constants.ID_PATH));
                    return invoiceUseCase.deleteById(id)
                            .then(ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS("ELIMINÉ", HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> deleteAlInvoicesByClient(ServerRequest request) {
        return validator.requirePathVariables(request, Set.of(Constants.ID_PATH))
                .flatMap(req -> {
                    UUID id = UUID.fromString(req.pathVariable(Constants.ID_PATH));
                    return invoiceUseCase.deleteAllByClientId(id)
                            .then(ServerResponse.ok()
                                    .bodyValue(StructResponse.structureRS("ELIMINÉ", HttpStatus.OK.value())));
                });
    }


    }
