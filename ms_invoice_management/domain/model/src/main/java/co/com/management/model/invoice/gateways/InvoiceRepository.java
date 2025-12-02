package co.com.management.model.invoice.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;

public interface InvoiceRepository {

    PageResult<Invoice> getAllByClientId(String id, int page, int size);

    PageResult<Invoice> getAllPageable(int page, int size);

    Invoice registerInvoice(Invoice invoice, Client client);

    Invoice getById(String id);

    void deleteInvoice(String id);

    void deleteAllByClientId(String clientId);

}
