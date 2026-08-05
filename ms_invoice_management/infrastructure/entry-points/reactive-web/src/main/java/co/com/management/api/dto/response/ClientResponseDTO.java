package co.com.management.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class ClientResponseDTO {
    private UUID id;
    private String documentNumber;
    private String documentType;

    @JsonProperty("isActive")
    private Boolean state;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
