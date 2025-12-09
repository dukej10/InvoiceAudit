package co.com.management.model.product.gateways;

import co.com.management.model.product.Product;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {

    Mono<Product> save(Product product, UUID invoiceId);
    
    Mono<Void> deleteAllByInvoice(UUID invoiceId);
}
