package co.com.management.api.handler;

import co.com.management.api.RequestValidator;
import co.com.management.api.Utility;
import co.com.management.api.dto.mapper.RequestMapper;
import co.com.management.api.dto.mapper.ResponseMapper;
import co.com.management.api.dto.request.InvoiceDTO;
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

    private final RequestValidator validator;

    private static final Set<String> PAGINATION_PARAMS = Set.of("page", "size");

    private final InvoiceUseCase invoiceUseCase;

    public Mono<ServerResponse> saveInvoice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(InvoiceDTO.class)
                .flatMap(validator::validateDto)
                .map(RequestMapper::toModel)
                .flatMap(invoiceUseCase::createInvoice)
                .flatMap(invoiceSaved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Utility.structureRS(invoiceSaved, HttpStatus.OK.value())));
    }

    public Mono<ServerResponse> getInvoicesByClientPageable(ServerRequest request) {
        return validator.requireParamsAndPathVars(request, PAGINATION_PARAMS, Set.of("clientId"))
                .flatMap(req -> {
                    int page = Integer.parseInt(req.queryParam("page").get());
                    int size = Integer.parseInt(req.queryParam("size").orElse("10"));
                    UUID clientId = UUID.fromString(req.pathVariable("clientId"));

                    return invoiceUseCase.getAllByClientId(clientId, page, size)
                            .map(ResponseMapper::toPageResultInvoiceDTO)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(Utility.structureRS(dto, HttpStatus.OK.value())));
                });
    }

    public Mono<ServerResponse> getInvoicesPageable(ServerRequest request) {
        return validator.requirePathVariables(request, PAGINATION_PARAMS)
                .flatMap(req -> {
                    int page = Integer.parseInt(req.queryParam("page").get());
                    int size = Integer.parseInt(req.queryParam("size").orElse("10"));

                    return invoiceUseCase.getAllPageable(page, size)
                            .map(ResponseMapper::toPageResultInvoiceDTO)
                            .flatMap(dto -> ServerResponse.ok()
                                    .bodyValue(Utility.structureRS(dto, HttpStatus.OK.value())));
                });
    }


}
