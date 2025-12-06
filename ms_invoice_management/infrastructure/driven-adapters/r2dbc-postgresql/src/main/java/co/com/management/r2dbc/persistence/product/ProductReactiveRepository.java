package co.com.management.r2dbc.persistence.product;

import co.com.management.r2dbc.persistence.invoice.InvoiceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, UUID>,
        ReactiveSortingRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findAllByInvoiceId(UUID invoiceId);


}
