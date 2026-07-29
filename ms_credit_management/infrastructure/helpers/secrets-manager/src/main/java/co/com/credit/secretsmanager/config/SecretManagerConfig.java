package co.com.credit.secretsmanager.config;

import co.com.bancolombia.secretsmanager.config.AWSSecretsManagerConfig;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnectorAsync;
import co.com.credit.secretsmanager.config.properties.SecretManagerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;

@Configuration
public class SecretManagerConfig {
    private final SecretManagerProperties properties;
    private final String region;

    public SecretManagerConfig(SecretManagerProperties properties,
                               @Value("${aws.region}") String region
    ){
        this.properties = properties;
        this.region = region;
    }

    @Bean
    @Profile({"local"})
    public AWSSecretManagerConnectorAsync localConnectionAws() {
        return new AWSSecretManagerConnectorAsync(
                AWSSecretsManagerConfig.builder()
                        .endpoint(properties.getEndpoint())
                        .cacheSeconds(properties.getCacheTime())
                        .cacheSize(properties.getCacheSize())
                        .region(Region.of(region))
                        .build()
        );
    }

    @Bean
    @Profile({"dev", "qa", "pdn"})
    public AWSSecretManagerConnectorAsync connectionAws() {
        return new AWSSecretManagerConnectorAsync(
                AWSSecretsManagerConfig.builder()
                        .cacheSeconds(properties.getCacheTime())
                        .cacheSize(properties.getCacheSize())
                        .region(Region.of(region))
                        .build()
        );
    }


}