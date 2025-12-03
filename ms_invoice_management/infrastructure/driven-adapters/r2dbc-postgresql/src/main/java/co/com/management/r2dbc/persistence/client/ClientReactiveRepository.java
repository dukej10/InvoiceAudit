package co.com.management.r2dbc.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface ClientReactiveRepository extends ReactiveCrudRepository<ClientEntity, String>,
        ReactiveSortingRepository<ClientEntity, String> {
    Mono<ClientEntity> findByDocumentNumberAndDocumentType(
            String documentNumber,
            String documentType);

}
