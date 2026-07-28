package co.com.management.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "management.adapters.postgresql")
public record PostgreSQLConnectionPoolProperties(
        String schema,
        int initialSize,
        int maxSize,
        int maxIdleTime
) {
}
