package co.com.credit.dynamodb;

import co.com.credit.dynamodb.helper.TemplateAdapterOperations;
import co.com.credit.dynamodb.models.ClientEntity;
import co.com.credit.model.credit.Credit;
import co.com.credit.model.credit.gateways.CreditRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ExecuteStatementRequest;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Credit, String, ClientEntity>
        implements CreditRepository {

    private final DynamoDbAsyncClient lowLevelClient;

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient enhancedClient,
                                   DynamoDbAsyncClient lowLevelClient,
                                   ObjectMapper mapper) {
        super(enhancedClient, mapper, d -> mapper.map(d, Credit.class), "credit_client");
        this.lowLevelClient = lowLevelClient;
    }

    @Override
    public Mono<Credit> findByClientId(String clientId) {
        return super.getById(clientId); // ← Ahora funciona perfecto porque PK = client_id
    }

    @Override
    public Mono<Credit> saveCredit(Credit credit) {
        return super.save(credit);
    }


    public Mono<List<Credit>> findAllActiveCredits() {
        String stmt = "SELECT * FROM \"credit_client\" WHERE credit_state = true";
        return executePartiqlList(stmt);
    }

    public Mono<List<Credit>> findCreditsWithAvailableLimitGreaterThan(BigDecimal amount) {
        String stmt = "SELECT * FROM \"credit_client\" WHERE current_credit_limit > ?";
        return executePartiqlList(stmt, AttributeValue.fromN(amount.toPlainString()));
    }

    public Mono<Long> countActiveCredits() {
        String stmt = "SELECT count(*) FROM \"credit_client\" WHERE credit_state = true";
        return Mono.fromFuture(lowLevelClient.executeStatement(r -> r
                        .statement(stmt)))
                .map(response -> response.items().get(0)
                        .values().stream().findFirst()
                        .map(av -> Long.parseLong(av.n()))
                        .orElse(0L));
    }

    // ====================== HELPER PRIVADO (solo PartiQL) ======================
    private Mono<List<Credit>> executePartiqlList(String statement, AttributeValue... params) {
        return Mono.fromFuture(lowLevelClient.executeStatement(ExecuteStatementRequest.builder()
                        .statement(statement)
                        .parameters(List.of(params))
                        .consistentRead(true)
                        .build()))
                .flatMapIterable(response -> response.items())
                .map(item -> mapper.map(item, ClientEntity.class))
                .map(entity -> mapper.map(entity, Credit.class))
                .collectList();
    }
}