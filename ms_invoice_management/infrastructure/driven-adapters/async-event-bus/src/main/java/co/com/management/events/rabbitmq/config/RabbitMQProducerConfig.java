package co.com.management.events.rabbitmq.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.BindingSpecification;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
@RequiredArgsConstructor
public class RabbitMQProducerConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducerConfig.class);

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
    public Mono<Connection> rabbitConnection(ConnectionFactory factory) {
        return Mono.fromCallable(factory::newConnection)
                .subscribeOn(Schedulers.boundedElastic())
                .cache();
    }

    @Bean
    public Sender sender(Mono<Connection> connectionMono) {
        SenderOptions options = new SenderOptions().connectionMono(connectionMono);
        return RabbitFlux.createSender(options);
    }

    // Declarar exchange, queue y binding (idempotente)
    @Bean
    public Mono<Void> declareQueueAndExchange(Sender sender) {
        return sender.declare(ExchangeSpecification.exchange(rabbitMQConnection.getExchange()).type("direct")
                        .durable(true))
                .then(sender.declare(QueueSpecification.queue(rabbitMQConnection.getQueue()).durable(true)))
                .then(sender.bind(BindingSpecification.binding(rabbitMQConnection.getExchange(),
                        rabbitMQConnection.getRoutingKey(), rabbitMQConnection.getQueue())))
                .doOnSuccess(v -> log.info("RabbitMQ infrastructure declared successfully"))
                .doOnError(e -> log.error("Error declaring RabbitMQ infrastructure", e)).then();
    }
}