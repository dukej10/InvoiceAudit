package co.com.management.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .exceptionHandling(exception -> exception

                        // 401 → No autenticado (sin token o token inválido)
                        .authenticationEntryPoint((exchange, ex) -> {
                            return invalid(exchange, HttpStatus.UNAUTHORIZED, """
                                    {
                                      "code": 401,
                                      "message": "Token inválido"
                                    }
                                    """);
                        })

                        // 403 → Autenticado pero sin permisos
                        .accessDeniedHandler((exchange, ex) -> {
                            return invalid(exchange, HttpStatus.FORBIDDEN, """
                                    {
                                      "code": 403,
                                      "message": "Acceso denegado"
                                    }
                                    """);
                        })
                )

                .authorizeExchange(exchange -> exchange

                        // Público
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/**")
                        .permitAll()

                        // Restricción
                        .pathMatchers("/api/v1/clients/**")
                        .hasAnyRole("ADMIN", "USER")

                        .pathMatchers("/api/v1/invoices/**")
                        .hasRole("ADMIN")

                        .anyExchange().authenticated()
                )

                .addFilterAt(
                        jwtAuthenticationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .build();
    }

    private static Mono<Void> invalid(ServerWebExchange exchange, HttpStatus unauthorized, String body) {
        var response = exchange.getResponse();
        response.setStatusCode(unauthorized);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
