package co.com.management.model.invoice;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Invoice {
    private String id;
    private BigDecimal totalAmount;
    private List<Product> products;
    private LocalDateTime createdDate;
    private String clientId;
}
