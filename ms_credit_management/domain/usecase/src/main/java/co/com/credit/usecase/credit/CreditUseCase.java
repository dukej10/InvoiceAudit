package co.com.credit.usecase.credit;

import co.com.credit.model.credit.Credit;
import co.com.credit.model.credit.gateways.CreditRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreditUseCase {

    private final CreditRepository creditRepository;

    public Mono<Credit> saveCredit(String clientId, BigDecimal amount) {
        return creditRepository.findByClientId(clientId)
                .map(existing -> {
                    existing.setCurrentCreditLimit(existing.getCurrentCreditLimit().add(amount));
                    existing.setUpdatedDate(LocalDateTime.now());
                    return existing;
                })
                .defaultIfEmpty(
                        Credit.builder()
                                .clientId(clientId)
                                .initialCreditLimit(amount)
                                .currentCreditLimit(amount)
                                .creditState(true)
                                .createdDate(LocalDateTime.now())
                                .updatedDate(LocalDateTime.now())
                                .build()
                )
                .flatMap(creditRepository::saveCredit);
    }

    public Mono<Credit> closeCredit (String clientId){
        return creditRepository.findByClientId(clientId)
                .map(existing -> {
                    existing.setCreditState(false);
                    existing.setUpdatedDate(LocalDateTime.now());
                    return existing;
                }).flatMap(creditRepository::saveCredit);
    }
}
