package co.com.credit.events.handlers;

import co.com.credit.usecase.credit.CreditUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreditEventConsumer {

    private final Receiver receiver;
    private final ObjectMapper objectMapper;
    private final CreditUseCase creditUseCase;

    @Value("${rabbitmq.queue:audit-queue}")
    private String queue;

    @EventListener(ApplicationReadyEvent.class)
    public void startConsuming() {
        receiver.consumeAutoAck(queue)
                .map(delivery -> new String(delivery.getBody(), StandardCharsets.UTF_8))
                .doOnNext(json -> log.info("[RABBITMQ] Crédito recibido: {}", json))
                .flatMap(this::processMessage)
                .doOnNext(v -> log.info("Crédito aplicado correctamente"))
                .onErrorContinue((err, obj) -> log.error("Error procesando mensaje", err))
                .subscribe();

        log.info("Consumidor de eventos de crédito iniciado → cola: {}", queue);
    }

    private Mono<Void> processMessage(String json) {
        return Mono.justOrEmpty(parseEvent(json))
                .flatMap(event ->
                        creditUseCase.saveCredit(event.clientId(), event.totalAmount())
                                .doOnSuccess(creditResult -> log.info("[CREDIT SUCCESS] Crédito aplicado → " +
                                                "clientId: {}",
                                                creditResult.getClientId())
                                )
                                .doOnError(e ->
                                        log.error("[CREDIT ERROR] Fallo al aplicar crédito → clientId: {}, monto: {}, " +
                                                        "error: {}",
                                                event.clientId(), event.totalAmount(), e.getMessage(), e)
                                )
                                .onErrorResume(e -> Mono.empty()) // auto-ack → mensaje ya consumido
                                .then(Mono.just(event)) // para que siga el flujo si necesitas algo más
                )
                .then();
    }

    private CreditEvent parseEvent(String json) {
        try {
            return objectMapper.readValue(json, CreditEvent.class);
        } catch (Exception e) {
            log.warn("JSON inválido descartado: {}", json);
            return null;
        }
    }
}
