package co.com.credit.dynamodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Bean
    @Profile("local")
    public DynamoDbAsyncClient dynamoDbAsyncClientLocal(
            @Value("${aws.dynamodb.endpoint}") String endpoint,
            @Value("${aws.region}") String region) {

        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .build();
    }

    @Bean
    @Profile({"dev", "cer", "pdn"})
    public DynamoDbAsyncClient dynamoDbAsyncClientCloud(@Value("${aws.region}") String region) {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }

    @Bean
    @Primary
    public DynamoDbAsyncClient dynamoDbAsyncClient(
            @Value("${aws.dynamodb.endpoint:}") String endpoint) {

        if (!endpoint.isBlank()) {
            // Perfil local
            return dynamoDbAsyncClientLocal(endpoint, "us-east-1");
        } else {
            // Perfiles cloud
            return dynamoDbAsyncClientCloud("us-east-1");
        }
    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }
}