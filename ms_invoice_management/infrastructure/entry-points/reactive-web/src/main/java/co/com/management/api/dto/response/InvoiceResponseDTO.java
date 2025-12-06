package co.com.management.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class InvoiceResponseDTO {

    private UUID id;

    private UUID clientId;

    List<ProductResponseDTO> products;

    LocalDateTime createdDate;
}
