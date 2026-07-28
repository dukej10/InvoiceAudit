package co.com.credit.dynamodb.config;

import co.com.credit.secretsmanager.config.properties.SecretManagerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;
import java.time.Duration;

@Configuration
public class DynamoDBConfig {
    private final SecretManagerProperties secretProperties;
    private final DynamoDBConnectionProperties dynamoProperties;
    private final String region;
    private final String endpoint;


    public DynamoDBConfig(SecretManagerProperties secretProperties,
                          DynamoDBConnectionProperties dynamoProperties,
                          @Value("${aws.region}") String region,
                          @Value("${aws.dynamodb.endpoint}") String endpoint) {
        this.secretProperties = secretProperties;
        this.dynamoProperties =  dynamoProperties;
        this.region = region;
        this.endpoint = endpoint;
    }
    @Bean
    @Profile("local")
    public DynamoDbAsyncClient dynamoDbAsyncClientLocal(
    ) {

        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(dynamoProperties.getAccessKey(),
                                        dynamoProperties.getSecretKey())
                        )
                )
                .overrideConfiguration(
                        ClientOverrideConfiguration.builder()
                                .apiCallTimeout(Duration.ofSeconds(5))
                                .apiCallAttemptTimeout(Duration.ofSeconds(3))
                                .build()
                )
                .build();
    }

    @Bean
    @Profile({"dev", "cer", "pdn"})
    public DynamoDbAsyncClient dynamoDbAsyncClientCloud() {

        return DynamoDbAsyncClient.builder()
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }
}
