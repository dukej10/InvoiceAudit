package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;

    private final InvoiceRepository invoiceRepository;

    public Mono<Client> saveClient(Client client) {
        client.setCreatedDate(LocalDateTime.now());
        return clientRepository.findByDocumentNumberAndDocumentType(client.getDocumentNumber(),
                client.getDocumentType()).switchIfEmpty(
                     clientRepository.saveClient(client)
        );
    }

    public Mono<Client> updateClient(Client client) {
        client.setUpdatedDate(LocalDateTime.now());
        return clientRepository.findById(client.getId())
                .flatMap(clientFound -> clientRepository.saveClient(client));
    }

    public Mono<Client> findByInfoDocument(String documentNumber, String documentType) {
        return clientRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType);
    }

    public Mono<Void> deleteById(UUID id) {
        return invoiceRepository.deleteAllByClientId(id).then(
                clientRepository.deleteClient(id)
        );
    }

    public Mono<PageResult<Client>> getAllPageable(int page, int size) {
        return clientRepository.findAllPageable(page, size);
    }

    public Mono<Client> getById(UUID id) {
        return clientRepository.findById(id);
    }

}
