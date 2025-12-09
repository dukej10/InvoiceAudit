package co.com.management.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductResponseDTO {

    private UUID id;

    private String name;

    private Integer quantity;

    private BigDecimal unitPrice;
}
