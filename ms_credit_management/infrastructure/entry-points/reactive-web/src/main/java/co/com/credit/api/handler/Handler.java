package co.com.credit.api.handler;

import co.com.credit.api.dto.CreditDTO;
import co.com.credit.usecase.credit.CreditUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CreditUseCase creditUseCase;
    private final RequestValidator validator;

    public Mono<ServerResponse> saveCredit(ServerRequest request) {
        return request.bodyToMono(CreditDTO.class)
                .flatMap(validator::validateDto)
                .flatMap(dto -> creditUseCase.saveCredit(dto.getClientId(), dto.getAmount()))
                .flatMap(credit -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(credit));
    }


}
