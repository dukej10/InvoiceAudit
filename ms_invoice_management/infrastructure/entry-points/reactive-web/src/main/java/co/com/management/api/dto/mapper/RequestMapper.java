package co.com.management.api.dto.mapper;

import co.com.management.api.dto.request.ClientDTO;
import co.com.management.api.dto.request.InvoiceDTO;
import co.com.management.api.dto.request.ProductDTO;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {
    public Client toModel(ClientDTO clientDTO){
        return Client.builder()
                .id(clientDTO.getId())
                .documentNumber(clientDTO.getDocumentNumber())
                .state(clientDTO.getState())
                .email(clientDTO.getEmail())
                .address(clientDTO.getAddress())
                .phone(clientDTO.getPhone())
                .documentType(clientDTO.getDocumentType())
                .lastName(clientDTO.getLastName())
                .firstName(clientDTO.getFirstName())
                .build();
    }

    public Invoice toModel(InvoiceDTO invoiceDTO){
        return Invoice.builder()
                .clientId(invoiceDTO.getClientId())
                .products(
                        invoiceDTO.getProducts().stream()
                                .map(RequestMapper::toModel)
                                .toList()
                )
                .build();
    }

    private Product toModel(ProductDTO productDTO){
        return Product.builder()
                .name(productDTO.getName())
                .unitPrice(productDTO.getUnitPrice())
                .quantity(productDTO.getQuantity())
                .build();
    }
}
