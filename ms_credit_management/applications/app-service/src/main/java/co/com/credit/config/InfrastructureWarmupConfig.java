package co.com.credit.config;

import co.com.credit.model.credit.gateways.CreditRepository;
import com.rabbitmq.client.Connection;
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

    private static final Duration DYNAMO_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration RABBIT_TIMEOUT = Duration.ofSeconds(5);

    private static final String WARMUP_DUMMY_ID = "00000000-0000-0000-0000-000000000000";



    @Bean
    @Order(1)
    public CommandLineRunner warmupInfrastructure(
            CreditRepository creditRepository,
            Mono<Connection> connection
    ) {
        return args -> {
            warmupDynamoDB(creditRepository);
            warmupRabbitMQ(connection);
        };
    }

    private void warmupDynamoDB(CreditRepository creditRepository) {
        try {
            creditRepository.findByClientId(WARMUP_DUMMY_ID)
                    .timeout(DYNAMO_TIMEOUT)
                    .doOnSuccess(credit -> log.info("DynamoDB: Cliente y conexión validados correctamente"))
                    .doOnError(error -> log.warn("DynamoDB Warmup falló: {}", error.getMessage()))
                    .onErrorResume(error -> Mono.empty())
                    .block();

        } catch (Exception e) {
            log.warn("Error no controlado durante el warmup de DynamoDB", e);
        }
    }

    @SuppressWarnings("try")
    private void warmupRabbitMQ(Mono<Connection> connection) {
        try {
            connection
                    .timeout(RABBIT_TIMEOUT)
                    .doOnSuccess(c ->
                            log.info("RabbitMQ Consumidor: Conexión establecida correctamente"))
                    .doOnError(e ->
                            log.warn("RabbitMQ Consumidor Warmup falló: {}", e.getMessage()))
                    .onErrorResume(e -> Mono.empty())
                    .block();

        } catch (Exception e) {
            log.warn("Error no controlado durante el warmup de RabbitMQ en el consumidor", e);
        }
    }
}