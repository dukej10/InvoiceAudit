package co.com.management.r2dbc.persistence.client;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClientReactiveRepository extends ReactiveCrudRepository<ClientEntity, UUID>,
        ReactiveSortingRepository<ClientEntity, UUID> {
    Mono<ClientEntity> findByDocumentNumberAndDocumentType(
            String documentNumber,
            String documentType);

}
