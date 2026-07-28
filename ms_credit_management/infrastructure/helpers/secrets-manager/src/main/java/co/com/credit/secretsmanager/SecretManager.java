package co.com.credit.secretsmanager;

import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnectorAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecretManager {

    private final AWSSecretManagerConnectorAsync awsSecretManagerConnectorAsync;

    public SecretManager(AWSSecretManagerConnectorAsync awsSecretManagerConnectorAsync) {
        this.awsSecretManagerConnectorAsync = awsSecretManagerConnectorAsync;
    }

    public <T> T getSecret(String secret, Class<T> cls) {
        return awsSecretManagerConnectorAsync.getSecret(secret, cls)
                .doOnError(error ->
                        log.error("Error retrieving secret '{}': {}", secret, error.getMessage(), error)
                )
                .block();
    }
}