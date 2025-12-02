package co.com.management.api.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder(toBuilder = true)
public class ClientResponseDTO {
    private String documentNumber;
    private String documentType;
    private Boolean state;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<InvoiceResponseDTO> invoices;
}
