package co.com.credit.model.credit;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Credit {
    private String clientId;
    private Boolean creditState;
    private BigDecimal initialCreditLimit;
    private BigDecimal currentCreditLimit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
