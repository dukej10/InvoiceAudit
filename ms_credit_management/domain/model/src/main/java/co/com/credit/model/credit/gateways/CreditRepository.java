package co.com.credit.model.credit.gateways;

import co.com.credit.model.credit.Credit;
import reactor.core.publisher.Mono;

public interface CreditRepository {

    Mono<Credit> saveCredit(Credit credit);

    Mono<Credit> findByClientId(String clientId);
}
