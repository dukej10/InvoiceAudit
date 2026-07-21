package co.com.management.secretsmanager.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("helpers.aws.secrets-manager")
@Getter
@Setter
public class SecretManagerProperties {
    private int cacheSize;
    private int cacheTime;
    private String region;
    private String endpoint;
}
