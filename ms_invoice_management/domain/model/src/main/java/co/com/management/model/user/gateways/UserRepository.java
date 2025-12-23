package co.com.management.model.user.gateways;

import co.com.management.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
    Mono<User> findByUsername(String username);
}
