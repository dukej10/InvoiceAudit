package co.com.management.events;

import co.com.management.model.events.BuyMessage;
import co.com.management.model.events.gateways.EventsGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class RabbitMQMessageSender  implements EventsGateway {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQMessageSender.class);

    private final Sender sender;
    private final ObjectMapper objectMapper;
    private final String exchange;
    private final String routingKey;

    public RabbitMQMessageSender(Sender sender, ObjectMapper objectMapper,
                                 @Value("${rabbitmq.exchange}") String exchange,
                                 @Value("${rabbitmq.routing-key}") String routingKey) {
        this.sender = sender;
        this.objectMapper = objectMapper;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public Mono<Void> send(BuyMessage message) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(message))
                .map(json -> new OutboundMessage(exchange, routingKey, json.getBytes(StandardCharsets.UTF_8)))
                .flatMap(msg -> sender.send(Mono.just(msg)))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .doBeforeRetry(rs -> log.warn("Retrying send message: attempt {}", rs.totalRetries())))
                .doOnSuccess(v -> log.info("Message sent successfully: {}", message))
                .doOnError(e -> log.error("Error sending message: {}", message, e));
    }

}
