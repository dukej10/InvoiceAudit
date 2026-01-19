package co.com.management.r2dbc.persistence.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table("users")
public class UserEntity {

    @Id
    private UUID id;

    private String username;

    private String password;

    private Boolean enabled;
}
