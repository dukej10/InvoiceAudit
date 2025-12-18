package co.com.credit.events.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreditEvent(

        @NotBlank
        @JsonProperty("clientId")
        String clientId,

        @Positive
        @JsonProperty("totalAmount")
        BigDecimal totalAmount

) {}
