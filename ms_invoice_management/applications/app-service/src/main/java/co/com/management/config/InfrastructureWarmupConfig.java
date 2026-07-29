package co.com.management.config;

import co.com.management.model.client.gateways.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Configuration
public class InfrastructureWarmupConfig {

    private static final Duration POSTGRES_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration RABBIT_TIMEOUT = Duration.ofSeconds(5);

    @Bean
    @Order(1)
    public CommandLineRunner warmupInfrastructure(
            ClientRepository clientRepository,
            Mono<Void> declareQueueAndExchange
    ) {
        return args -> {
            warmupPostgreSQL(clientRepository);
            warmupRabbitMQ(declareQueueAndExchange);
        };
    }

    private void warmupPostgreSQL(ClientRepository clientRepository) {
        try {
            clientRepository.findAllPageable(0, 1)
                    .timeout(POSTGRES_TIMEOUT)
                    .doOnNext(page ->
                            log.info("PostgreSQL: Pool de conexiones validado correctamente"))
                    .doOnError(error -> log.warn("PostgreSQL Warmup: {}", error.getMessage()))
                    .onErrorResume(error -> Mono.empty())
                    .block();

        } catch (Exception e) {
            log.warn("Error durante el warmup de PostgreSQL", e);
        }
    }

    private void warmupRabbitMQ(Mono<Void> declareQueueAndExchange) {
        try {
            declareQueueAndExchange
                    .timeout(RABBIT_TIMEOUT)
                    .doOnSuccess(v -> log.info("RabbitMQ: Conexión, exchange, queue y binding validados"))
                    .doOnError(error -> log.warn("RabbitMQ Warmup: {}", error.getMessage()))
                    .onErrorResume(error -> Mono.empty())
                    .block();

        } catch (Exception e) {
            log.warn("Error durante el warmup de RabbitMQ", e);
        }
    }
}