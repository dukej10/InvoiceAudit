package co.com.management.r2dbc.persistence.product;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, UUID>,
        ReactiveSortingRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findAllByInvoiceId(UUID invoiceId);


}
