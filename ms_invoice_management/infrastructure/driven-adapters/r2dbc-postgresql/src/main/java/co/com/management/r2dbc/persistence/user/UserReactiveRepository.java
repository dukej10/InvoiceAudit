package co.com.management.r2dbc.persistence.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, UUID>,
        ReactiveSortingRepository<UserEntity, UUID> {

    Mono<UserEntity> findByUsername(String username);

}
