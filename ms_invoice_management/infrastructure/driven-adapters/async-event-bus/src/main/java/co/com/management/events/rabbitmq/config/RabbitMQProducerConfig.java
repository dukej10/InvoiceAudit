package co.com.management.events.rabbitmq.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class RabbitMQProducerConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducerConfig.class);

    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.port}")
    private int port;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;
    @Value("${rabbitmq.queue}")
    private String queue;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public Mono<Connection> rabbitConnection(ConnectionFactory factory) {
        return Mono.fromCallable(factory::newConnection)
                .subscribeOn(Schedulers.boundedElastic())
                .cache();  // Cache para reutilización (senior: eficiencia)
    }

    @Bean
    public Sender sender(Mono<Connection> connectionMono) {
        SenderOptions options = new SenderOptions().connectionMono(connectionMono);
        return RabbitFlux.createSender(options);
    }

    // Declarar exchange, queue y binding (idempotente)
    @Bean
    public Mono<Void> declareQueueAndExchange(Sender sender) {
        return sender.declare(ExchangeSpecification.exchange(exchange).type("direct").durable(true))
                .then(sender.declare(QueueSpecification.queue(queue).durable(true)))
                .then(sender.bind(BindingSpecification.binding(exchange, routingKey, queue)))
                .doOnSuccess(v -> log.info("RabbitMQ infrastructure declared successfully"))
                .doOnError(e -> log.error("Error declaring RabbitMQ infrastructure", e)).then();
    }
}