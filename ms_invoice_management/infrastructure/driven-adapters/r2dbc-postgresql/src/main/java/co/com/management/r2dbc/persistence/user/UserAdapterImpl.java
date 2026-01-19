package co.com.management.r2dbc.persistence.user;

import co.com.management.model.user.User;
import co.com.management.model.user.gateways.UserRepository;
import co.com.management.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public class UserAdapterImpl extends ReactiveAdapterOperations<
        User, UserEntity, UUID, UserReactiveRepository>
        implements UserRepository {

    private final DatabaseClient databaseClient;


    public UserAdapterImpl(UserReactiveRepository repository, ObjectMapper mapper,
                           DatabaseClient databaseClient) {
        super(repository, mapper, dao -> mapper.map(dao, User.class));
        this.databaseClient = databaseClient;

    }


    @Override
    public Mono<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .filter(UserEntity::getEnabled)
                .flatMap(this::loadUserWithRoles);
    }

    private Mono<User> loadUserWithRoles(UserEntity entity) {
        User user = mapper.map(entity, User.class);

        return findRolesByUserId(entity.getId())
                .map(roles -> {
                    user.setRoles(roles);
                    return user;
                });
    }

    private Mono<List<String>> findRolesByUserId(UUID userId) {
        return databaseClient.sql("""
            SELECT r.name
            FROM roles r
            JOIN user_roles ur ON ur.role_id = r.id
            WHERE ur.user_id = :userId
        """)
                .bind("userId", userId)
                .map((row, meta) -> row.get("name", String.class))
                .all()
                .collectList();
    }
}