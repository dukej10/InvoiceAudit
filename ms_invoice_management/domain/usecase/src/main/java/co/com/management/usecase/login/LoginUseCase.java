package co.com.management.usecase.login;

import co.com.management.model.user.User;
import co.com.management.model.user.gateways.PasswordEncoderGateway;
import co.com.management.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoderGateway passwordEncoder;

    public Mono<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")));
    }

}
