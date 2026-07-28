package co.com.credit.dynamodb.config;

import co.com.credit.secretsmanager.SecretManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBSecretConfig {

    private final SecretManager secretManager;
    private final String secretName;

    public DynamoDBSecretConfig(
            SecretManager secretManager,
            @Value("${aws.secrets-manager.secrets.secret-dynamodb}")
            String secretName)
    {
        this.secretManager = secretManager;
        this.secretName = secretName;
    }

    @Bean
    public DynamoDBConnectionProperties getSecretDynamo() {
        return secretManager.getSecret(secretName, DynamoDBConnectionProperties.class);
    }
}
