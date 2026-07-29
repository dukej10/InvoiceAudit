package co.com.management.r2dbc.persistence.product;

import co.com.management.model.product.Product;
import co.com.management.model.product.gateways.ProductRepository;
import co.com.management.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ProductAdapterImpl extends ReactiveAdapterOperations<
        Product, ProductEntity, UUID, ProductReactiveRepository>
        implements ProductRepository {

    public ProductAdapterImpl(ProductReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, dao -> mapper.map(dao, Product.class));
    }


    @Override
    public Mono<Product> save(Product product, UUID invoiceId) {
        ProductEntity productEntity = toData(product);
        productEntity.setInvoiceId(invoiceId);
        return repository.save(productEntity)
                .map(this::toEntity);
    }

    private Flux<UUID> getProductIdByInvoice(UUID invoiceID) {
        return repository.findAllByInvoiceId(invoiceID)
                .map(ProductEntity::getId);
    }

    @Override
    public Mono<Void> deleteAllByInvoice(UUID invoiceID) {
        return getProductIdByInvoice(invoiceID).flatMap(
                x-> repository.deleteById(x)
        ).then();
    }



}