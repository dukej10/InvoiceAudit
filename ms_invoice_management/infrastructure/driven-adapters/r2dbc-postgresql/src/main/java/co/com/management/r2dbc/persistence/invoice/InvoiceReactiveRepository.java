package co.com.management.r2dbc.persistence.invoice;

import co.com.management.r2dbc.persistence.client.ClientEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InvoiceReactiveRepository extends ReactiveCrudRepository<InvoiceEntity, UUID>,
        ReactiveSortingRepository<InvoiceEntity, UUID> {
    Flux<InvoiceEntity> findByClientId(UUID clientId, Pageable pageable);


    Flux<InvoiceEntity> findAllByClientId(UUID clientId);

    Mono<Long> countByClientId(UUID clientId);

}
