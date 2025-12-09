package co.com.management.model.invoice;

import co.com.management.model.client.Client;
import co.com.management.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Invoice {
    private UUID id;
    private BigDecimal totalAmount;
    private List<Product> products;
    private LocalDateTime createdDate;
    private UUID clientId;
}
