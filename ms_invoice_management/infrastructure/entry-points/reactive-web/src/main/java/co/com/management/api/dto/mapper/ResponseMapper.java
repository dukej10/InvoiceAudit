package co.com.management.api.dto.mapper;

import co.com.management.api.dto.response.ClientResponseFullDTO;
import co.com.management.api.dto.response.InvoiceResponseDTO;
import co.com.management.api.dto.response.PageResultDTO;
import co.com.management.api.dto.response.ProductResponseDTO;
import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMapper {
    public ClientResponseFullDTO responseFull(Client client){
        return  ClientResponseFullDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .state(client.getState())
                .documentType(client.getDocumentType())
                .documentNumber(client.getDocumentNumber())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .createdDate(client.getCreatedDate())
                .updatedDate(client.getUpdatedDate())
                .build();
    }

    public InvoiceResponseDTO response(Invoice invoice){
        return  InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .clientId(invoice.getClientId())
                .createdDate(invoice.getCreatedDate())
                .build();
    }

    public InvoiceResponseDTO responseFull(Invoice invoice){
        return  InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .clientId(invoice.getClientId())
                .products(invoice.getProducts().stream()
                        .map(ResponseMapper::response).toList())
                .createdDate(invoice.getCreatedDate())
                .build();
    }

    private ProductResponseDTO response(Product product){
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .unitPrice(product.getUnitPrice())
                .build();
    }

    public PageResultDTO<ClientResponseFullDTO> toPageResultClientDTO(PageResult<Client> pageResult){
        return new PageResultDTO<> (
                pageResult.getItems().stream().map(ResponseMapper::responseFull).toList(),
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalItems(),
                pageResult.getTotalPages(),
                pageResult.isHasNext(),
                pageResult.isHasPrevious()
        );
    }

    public PageResultDTO<InvoiceResponseDTO> toPageResultInvoiceDTO(PageResult<Invoice> pageResult){
        return new PageResultDTO<> (
                pageResult.getItems().stream().map(ResponseMapper::response).toList(),
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalItems(),
                pageResult.getTotalPages(),
                pageResult.isHasNext(),
                pageResult.isHasPrevious()
        );
    }
}
