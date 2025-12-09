package co.com.management.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientDTO {

    @NotNull(groups = Update.class, message = "El código es obligatorio para actualizar")
    private UUID id;

    @NotBlank
    @Size(min = 5, max = 20, message = "El número de documento debe tener entre 5 y 20 caracteres")
    private String documentNumber;

    @NotBlank
    @Pattern(regexp = "^(CC|CE|NIT|TI)$", message = "Tipo de documento inválido. Valores permitidos: CC, NIT y TI")
    private String documentType;

    @NotNull
    private Boolean state;

    @NotBlank
    @Size(min = 4, max = 50, message = "El nombre debe tener entre 4 y 50 caracteres")
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 50, message = "El nombre debe tener entre 4 y 50 caracteres")
    private String lastName;

    @NotBlank
    @Email(message = "El email debe tener un formato válido")
    @Size(max=25)
    private String email;


    @NotBlank
    @Size(min = 10, max=13)
    private String phone;

    @NotBlank
    @Size(min = 5, max = 150, message = "La dirección debe tener entre 5 y 150 caracteres")
    private String address;

    public interface Update {}

}
