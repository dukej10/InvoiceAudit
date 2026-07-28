package co.com.management.r2dbc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostgreSQLConnectionProperties {
    private String host;
    private Integer port;
    private String dbname;
    private String username;
    private String password;
}
