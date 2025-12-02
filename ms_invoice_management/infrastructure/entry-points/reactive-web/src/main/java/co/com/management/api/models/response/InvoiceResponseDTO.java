package co.com.management.api.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class InvoiceResponseDTO {

    private String id;

    private String clientId;

    List<ProductResponseDTO> products;

    LocalDateTime createdDate;
}
