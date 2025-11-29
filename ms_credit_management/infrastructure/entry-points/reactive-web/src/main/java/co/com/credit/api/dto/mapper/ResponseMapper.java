package co.com.credit.api.dto.mapper;

import co.com.credit.api.dto.CreditDTO;
import co.com.credit.model.credit.Credit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMapper {
    public Credit toModel(CreditDTO creditDTO) {
        return Credit.builder()
                .clientId(creditDTO.getClientId())
                .build();
    }
}
