package co.com.management.api;

import co.com.management.api.handler.ClientHandler;
import co.com.management.api.handler.InvoiceHandler;
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
    private final InvoiceHandler invoiceHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .path(API_V1, api -> api
                        .nest(path("/clients"), clients -> clients
                                .GET("/all", clientHandler::getClientsPageable)
                                .POST("/save", clientHandler::saveClient)
                                .PUT("/update", clientHandler::updateClient)
                                .GET("/{id}", clientHandler::getClientById)
                                .GET("/infoDoc", clientHandler::getClientByInfoDoc)
                                .DELETE("/delete/{id}", clientHandler::deleteClient)
                        )

                        .nest(path("/invoices"), invoices -> invoices
                                .POST("/save", invoiceHandler::saveInvoice)
                                .GET("byClient/{clientId}", invoiceHandler::getInvoicesByClientPageable)
                                .DELETE("/delete/{id}", invoiceHandler::deleteInvoice)
                                .DELETE("/delete/all-by-client/{id}", invoiceHandler::deleteAlInvoicesByClient)
                                .GET("all", invoiceHandler::getInvoicesPageable)
                        )
                ).build();

    }
}
