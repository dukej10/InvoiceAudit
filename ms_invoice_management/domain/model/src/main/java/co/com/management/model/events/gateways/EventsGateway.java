package co.com.management.model.events.gateways;

import co.com.management.model.events.BuyMessage;
import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> send(BuyMessage message);
}
