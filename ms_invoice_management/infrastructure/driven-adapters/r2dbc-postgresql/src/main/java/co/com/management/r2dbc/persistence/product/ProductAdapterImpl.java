package co.com.management.r2dbc.persistence.product;

import co.com.management.model.PageResult;
import co.com.management.model.exception.BusinessException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.model.product.Product;
import co.com.management.model.product.gateways.ProductRepository;
import co.com.management.r2dbc.helper.ReactiveAdapterOperations;
import co.com.management.r2dbc.persistence.invoice.InvoiceEntity;
import co.com.management.r2dbc.persistence.invoice.InvoiceReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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