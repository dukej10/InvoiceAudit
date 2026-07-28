package co.com.credit.events.rabbit.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQReceiverConfig {

    private final RabbitMQConnectionProperties rabbitMQConnection;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQConnection.getHost());
        factory.setPort(rabbitMQConnection.getPort());
        factory.setUsername(rabbitMQConnection.getUsername());
        factory.setPassword(rabbitMQConnection.getPassword());
        return factory;
    }

    @Bean
    public Mono<Connection> connection(ConnectionFactory factory) {
        return Mono.fromCallable(factory::newConnection)
                .subscribeOn(Schedulers.boundedElastic())
                .cache();
    }

    @Bean
    public Receiver receiver(Mono<Connection> connectionMono) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
    }

}
