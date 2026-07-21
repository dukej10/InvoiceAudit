package co.com.management.secretsmanager;

import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnectorAsync;
import org.springframework.stereotype.Component;

@Component
public class SecretManager {

    private final AWSSecretManagerConnectorAsync awsSecretManagerConnectorAsync;

    public SecretManager(AWSSecretManagerConnectorAsync awsSecretManagerConnectorAsync) {
        this.awsSecretManagerConnectorAsync = awsSecretManagerConnectorAsync;
    }

    public <T> T getSecret(String secret, Class<T> cls){
        return awsSecretManagerConnectorAsync.getSecret(secret, cls)
                .block();
    }
}
