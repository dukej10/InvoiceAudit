package co.com.management.model.client.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ClientRepository {
    Mono<Client> saveClient(Client client);

    Mono<Void> deleteClient(UUID id);

    Mono<Client> findById(UUID id);

    Mono<Client> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    Mono<PageResult<Client>> findAllPageable(int page, int size);

}
