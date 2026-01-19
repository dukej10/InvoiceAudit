package co.com.management.r2dbc.persistence.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table("user_roles")
public class UserRoleEntity {

    private UUID userId;
    private UUID roleId;
}
