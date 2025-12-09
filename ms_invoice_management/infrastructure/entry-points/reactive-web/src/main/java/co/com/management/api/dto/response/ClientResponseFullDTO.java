package co.com.management.api.dto.response;

import co.com.management.model.invoice.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientResponseFullDTO {
    private UUID id;
    private String documentNumber;
    private String documentType;
    private Boolean state;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<Invoice> invoices;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
