package co.com.management.usecase.client;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;

    public Mono<Client> saveClient(Client client) {
        return clientRepository.findByDocumentNumberAndDocumentType(client.getDocumentNumber(),
                client.getDocumentType()).switchIfEmpty(
                     clientRepository.saveClient(client)
        );
    }

    public Mono<Client> updateClient(Client client) {
        return clientRepository.findById(client.getId())
                .flatMap(clientFound -> clientRepository.saveClient(client));
    }

    public Mono<Client> findByInfoDocument(String documentNumber, String documentType) {
        return clientRepository.findByDocumentNumberAndDocumentType(documentNumber, documentType);
    }

    public Mono<Void> deleteById(UUID id) {
        return clientRepository.deleteClient(id);
    }

    public Mono<PageResult<Client>> getAllPageable(int page, int size) {
        return clientRepository.findAllPageable(page, size);
    }

    public Mono<Client> getById(UUID id) {
        return clientRepository.findById(id);
    }

}
