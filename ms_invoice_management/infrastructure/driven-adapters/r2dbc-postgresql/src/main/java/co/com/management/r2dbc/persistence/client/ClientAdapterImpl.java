package co.com.management.r2dbc.persistence;

import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.client.gateways.ClientRepository;
import co.com.management.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ClientAdapterImpl extends ReactiveAdapterOperations<
        Client, ClientEntity, String, ClientReactiveRepository>
        implements ClientRepository {

    public ClientAdapterImpl(ClientReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, dao -> mapper.map(dao, Client.class));
    }

    @Override
    public Mono<Client> saveClient(Client client) {
        return this.save(client);
    }

    @Override
    public Mono<Void> deleteClient(String id) {
        return findById(id)
                .flatMap(client -> this.deleteById(client.getId()));
    }

    @Override
    public Mono<Client> findByDocumentNumberAndDocumentType(String documentNumber, String documentType) {
        return repository.findByDocumentNumberAndDocumentType(documentNumber, documentType)
                .map(super::toEntity);
    }

    @Override
    public Mono<PageResult<Client>> findAllPageable(int page, int size) {
        return super.findAll(page, size, Sort.by("documentNumber").ascending())
                .map(this::toPageResult);
    }

    private PageResult<Client> toPageResult(Page<Client> page) {
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