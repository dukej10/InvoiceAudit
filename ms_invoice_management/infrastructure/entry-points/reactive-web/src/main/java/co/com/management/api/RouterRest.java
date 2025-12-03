package co.com.management.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
@RequiredArgsConstructor
public class RouterRest {
    private static final String API_V1 = "/api/v1";
    private final ClientHandler clientHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .path(API_V1, api -> api
                        .nest(path("/clients"), clients -> clients
                                .GET("/all", clientHandler::getClientsPageable)
                                .POST("/save", clientHandler::saveClient)
                        )
                ).build();

    }
}
