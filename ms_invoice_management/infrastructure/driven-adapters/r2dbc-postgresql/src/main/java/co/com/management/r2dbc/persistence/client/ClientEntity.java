package co.com.management.r2dbc.persistence.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("clients")
@Getter
@Setter
@NoArgsConstructor
public class ClientEntity {

    @Id
    private String id;

    @Column("document_number")
    private String documentNumber;

    @Column("document_type")
    private String documentType;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    private Boolean state = true;

    private String email;

    private String phone;

    private String address;

    @Column("created_at")
    private LocalDateTime createdDate;

    @Column("updated_at")
    private LocalDateTime updatedDate;

}