package co.com.management.r2dbc.persistence.invoice;

import co.com.management.model.PageResult;
import co.com.management.model.exception.BusinessException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.model.product.Product;
import co.com.management.model.product.gateways.ProductRepository;
import co.com.management.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class InvoiceAdapterImpl extends ReactiveAdapterOperations<
        Invoice, InvoiceEntity, UUID, InvoiceReactiveRepository>
        implements InvoiceRepository {

    private final ProductRepository productRepository;
    public InvoiceAdapterImpl(InvoiceReactiveRepository repository, ObjectMapper mapper,
                              ProductRepository productRepository) {
        super(repository, mapper, dao -> mapper.map(dao, Invoice.class));
        this.productRepository = productRepository;
    }


    @Override
    public Mono<PageResult<Invoice>> getAllByClientId(UUID clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());

        return findPageByCriteria(
                repository.findByClientId(clientId, pageable),
                repository.countByClientId(clientId),
                pageable
        )
                .map(this::toPageResult)
                .switchIfEmpty(Mono.error(new BusinessException("No hay información")));
    }

    @Override
    public Mono<PageResult<Invoice>> getAllPageable(int page, int size) {
        return super.findAll(page, size, Sort.by("createdAt").descending())
                .map(this::toPageResult);
    }

    @Override
    public Mono<Invoice> registerInvoice(Invoice invoice) {
        return this.save(invoice).flatMap(
                invoiceSaved ->
                    Flux.fromIterable(invoice.getProducts())
                            .flatMap(product -> productRepository.save(product, invoiceSaved.getId()))
                            .collectList()
                            .map(productsSaved -> {
                                invoiceSaved.setProducts(productsSaved);
                                return invoiceSaved;
                            })
        );
    }

    @Override
    public Mono<Invoice> getById(UUID id) {
        return super.findById(id).switchIfEmpty(Mono.error(new BusinessException("No se encontró la factura")));
    }

    @Override
    public Mono<Void> deleteInvoice(UUID id) {
        return  this.getById(id).flatMap(invoice -> super.deleteById(invoice.getId()));
    }

    private Flux<Invoice> findAllByClientId(UUID clientId) {
        return repository.findAllByClientId(clientId).map(this::toEntity);
    }

    @Override
    public Mono<Void> deleteAllByClientId(UUID clientId) {
        return findAllByClientId(clientId)
                .map(Invoice::getId)
                .flatMap(invoiceId ->
                        productRepository.deleteAllByInvoice(invoiceId)
                                .then(repository.deleteById(invoiceId))
                )
                .then();
    }

    private PageResult<Invoice> toPageResult(Page<Invoice> page) {
        return new PageResult<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}