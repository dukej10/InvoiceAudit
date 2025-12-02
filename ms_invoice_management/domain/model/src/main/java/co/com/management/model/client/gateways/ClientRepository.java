package co.com.management.model.client.gateways;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import reactor.core.publisher.Mono;

public interface ClientRepository {
    Mono<Client> saveClient(Client client);

    Mono<Void> deleteClient(String id);

    Mono<Client> findById(String id);

    Mono<Client> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);

    Mono<PageResult<Client>> findAllPageable(int page, int size);

}
