package co.com.management.events.rabbitmq.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQConnectionProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String queue;
    private String exchange;
    private String routingKey;
}
