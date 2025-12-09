package co.com.management.model.invoice.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InvoiceRepository {

    Mono<PageResult<Invoice>> getAllByClientId(UUID clientId, int page, int size);

    Mono<PageResult<Invoice>> getAllPageable(int page, int size);

    Mono<Invoice> registerInvoice(Invoice invoice);

    Mono<Invoice> getById(UUID id);

    Mono<Void> deleteInvoice(UUID id);

    Mono<Void> deleteAllByClientId(UUID clientId);

}
