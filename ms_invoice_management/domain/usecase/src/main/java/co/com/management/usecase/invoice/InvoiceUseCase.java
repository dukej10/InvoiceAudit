package co.com.management.usecase.invoice;

import co.com.management.model.PageResult;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class InvoiceUseCase {
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    public Mono<PageResult<Invoice>> getAllPageable(int page, int size) {
        return invoiceRepository.getAllPageable(page, size);
    }

    public Mono<PageResult<Invoice>> getAllByClientId(UUID clientId, int page, int size) {
        return  invoiceRepository.getAllByClientId(clientId, page, size);
    }

    public Mono<Invoice> createInvoice(Invoice invoice) {
        invoice.setTotalAmount(calculateTotalAmount(invoice.getProducts()));
        return invoiceRepository.registerInvoice(invoice);
    }

    public Mono<Void> deleteById(UUID id) {
        return invoiceRepository.deleteInvoice(id)
                .then(productRepository.deleteAllByInvoice(id));
    }

    public Mono<Void> deleteAllByClientId(UUID id) {
        return invoiceRepository.deleteAllByClientId(id);
    }

    private BigDecimal calculateTotalAmount(List<Product> products) {
        return products.stream()
                .map(product -> product.getUnitPrice()
                        .multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
