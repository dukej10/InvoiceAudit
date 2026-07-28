package co.com.management.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;
import java.util.Map;

@EnableR2dbcRepositories(basePackages = {
        "co.com.management.r2dbc.persistence.client",
        "co.com.management.r2dbc.persistence.invoice",
        "co.com.management.r2dbc.persistence.product"
})
@Configuration
@RequiredArgsConstructor
public class PostgreSQLConnectionPool {

    private final PostgreSQLConnectionPoolProperties connectionPoolProperties;


    @Bean
	public ConnectionPool getConnectionConfig(PostgreSQLConnectionProperties properties) {
		PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
                .host(properties.getHost())
                .port(properties.getPort())
                .database(properties.getDbname())
                .schema(connectionPoolProperties.schema())
                .options(Map.of("search_path", connectionPoolProperties.schema()))
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name("api-postgres-connection-pool")
                .initialSize(connectionPoolProperties.initialSize())
                .maxSize(connectionPoolProperties.maxSize())
                .maxIdleTime(Duration.ofMinutes(connectionPoolProperties.maxIdleTime()))
                .validationQuery("SELECT 1")
                .build();

		return new ConnectionPool(poolConfiguration);
	}
}