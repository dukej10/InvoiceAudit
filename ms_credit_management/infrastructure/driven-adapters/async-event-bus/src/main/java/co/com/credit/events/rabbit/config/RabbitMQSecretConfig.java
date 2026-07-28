package co.com.credit.events.rabbit.config;

import co.com.credit.secretsmanager.SecretManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSecretConfig {

    private final SecretManager secretManager;
    private final String secretName;

    public RabbitMQSecretConfig(
            SecretManager secretManager,
            @Value("${aws.secrets-manager.secrets.secret-rabbitmq}")
            String secretName)
    {
        this.secretManager = secretManager;
        this.secretName = secretName;
    }

    @Bean
    public RabbitMQConnectionProperties rabbitMQConnectionProperties() {
        return secretManager.getSecret(secretName, RabbitMQConnectionProperties.class);
    }

}
