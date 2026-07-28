package co.com.management.r2dbc.config;

import co.com.management.secretsmanager.SecretManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgreSQLSecretConfig {

    private final SecretManager secretManager;
    private final String secretName;

    public PostgreSQLSecretConfig(
            SecretManager secretManager,
            @Value("${aws.secrets-manager.secrets.secret-postgresql}")
            String secretName)
    {
        this.secretManager = secretManager;
        this.secretName = secretName;
    }

    @Bean
    public PostgreSQLConnectionProperties getSecret() {
        return secretManager.getSecret(secretName, PostgreSQLConnectionProperties.class);
    }

}
