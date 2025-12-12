package co.com.credit.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreditDTO {

    @NotBlank(message = "clientId is required")
    @Size(min = 1, max = 50, message = "clientId must be between 1 and 50 characters")
    private String clientId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", inclusive = false, message = "amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "amount must have max 15 digits and 2 decimals")
    private BigDecimal amount;
}