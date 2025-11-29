package co.com.credit.dynamodb.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("local")
public class DynamoDBLocalInitializer {

    private static final String TABLE_NAME = "credit_client";

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {
        log.info("🔍 Verificando existencia de tabla '{}'", TABLE_NAME);

        dynamoDbAsyncClient
                .listTables()
                .thenApply(ListTablesResponse::tableNames)
                .thenAccept(tables -> {
                    if (!tables.contains(TABLE_NAME)) {
                        log.warn("⚠️ Tabla '{}' no existe. Creándola...", TABLE_NAME);
                        createTable();
                    } else {
                        log.info("✅ Tabla '{}' ya existe.", TABLE_NAME);
                    }
                })
                .get(); // Bloquea solo al iniciar la app (correcto en @PostConstruct)
    }

    private void createTable() {

        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(TABLE_NAME)
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("client_id")
                                .keyType(KeyType.HASH) // PK
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("client_id")
                                .attributeType(ScalarAttributeType.S) // tipo String
                                .build()
                )
                .billingMode(BillingMode.PAY_PER_REQUEST) // ideal para local/dev
                .build();

        dynamoDbAsyncClient.createTable(request)
                .thenAccept(response ->
                        log.info("🎉 Tabla '{}' creada correctamente.", TABLE_NAME)
                )
                .exceptionally(e -> {
                    log.error("❌ Error creando la tabla '{}': {}", TABLE_NAME, e.getMessage(), e);
                    return null;
                });
    }
}
