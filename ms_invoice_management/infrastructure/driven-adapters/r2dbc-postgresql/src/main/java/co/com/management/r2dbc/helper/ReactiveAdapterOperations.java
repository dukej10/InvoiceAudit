package co.com.management.r2dbc.helper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.data.support.PageableExecutionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

public abstract class ReactiveAdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I> & ReactiveSortingRepository<D, I> > {
    protected R repository;
    protected ObjectMapper mapper;
    private final Class<D> dataClass;
    private final Function<D, E> toEntityFn;

    @SuppressWarnings("unchecked")
    protected ReactiveAdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn) {
        this.repository = repository;
        this.mapper = mapper;
        this.dataClass = extractDataClass();
        this.toEntityFn = toEntityFn;
    }

    @SuppressWarnings("unchecked")
    private Class<D> extractDataClass() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<D>) pt.getActualTypeArguments()[1];
    }

    protected D toData(E entity) {
        return entity != null ? mapper.map(entity, dataClass) : null;
    }

    protected E toEntity(D data) {
        return data != null ? toEntityFn.apply(data) : null;
    }

    public Mono<E> save(E entity) {
        return Mono.justOrEmpty(entity)
                .map(this::toData)
                .flatMap(repository::save)
                .map(this::toEntity);
    }

    public Flux<E> saveAll(Flux<E> entities) {
        return entities
                .map(this::toData)
                .transform(repository::saveAll)
                .map(this::toEntity);
    }

    public Mono<E> findById(I id) {
        return repository.findById(id)
                .map(this::toEntity);
    }

    public Flux<E> findAll() {
        return repository.findAll().map(this::toEntity);
    }

    public Mono<Void> deleteById(I id) {
        return repository.deleteById(id);
    }

    public Mono<Page<E>> findAll(Pageable pageable) {
        Pageable sortedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        Flux<E> contentFlux = repository.findAll(sortedPageable.getSort())
                .skip((long) sortedPageable.getPageNumber() * sortedPageable.getPageSize())
                .take(sortedPageable.getPageSize())
                .map(this::toEntity);

        Mono<Long> totalMono = repository.count();

        return Mono.zip(contentFlux.collectList(), totalMono)
                .map(tuple -> PageableExecutionUtils.getPage(
                        tuple.getT1(),
                        sortedPageable,
                        tuple::getT2
                ));
    }

    public Mono<Page<E>> findAll(int page, int size) {
        return findAll(PageRequest.of(page, size));
    }

    public Mono<Page<E>> findAll(int page, int size, Sort sort) {
        return findAll(PageRequest.of(page, size, sort));
    }
}
