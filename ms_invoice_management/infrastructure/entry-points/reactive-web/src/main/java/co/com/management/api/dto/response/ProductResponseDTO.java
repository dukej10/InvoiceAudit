package co.com.management.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class ProductResponseDTO {

    private  String id;

    private String name;

    private Integer quantity;

    private Double unitPrice;
}
