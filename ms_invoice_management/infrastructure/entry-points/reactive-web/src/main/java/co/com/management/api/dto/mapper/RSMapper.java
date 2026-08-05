package co.com.management.api.dto.mapper;

import co.com.management.api.dto.response.ClientResponseDTO;
import co.com.management.api.dto.response.InvoiceResponseDTO;
import co.com.management.api.dto.response.page.PageResultDTO;
import co.com.management.api.dto.response.ProductResponseDTO;
import co.com.management.api.dto.response.page.PaginationDTO;
import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RSMapper {

    ClientResponseDTO response(Client client);

    InvoiceResponseDTO response(Invoice invoice);

    ProductResponseDTO response(Product product);

    default PageResultDTO<ClientResponseDTO> toPageResultClientDTO(PageResult<Client> pageResult) {
        return new PageResultDTO<>(
                pageResult.getItems().stream()
                        .map(this::response)
                        .toList(),
                toPaginationDTO(pageResult)
        );
    }

    default PaginationDTO toPaginationDTO(PageResult<?> pageResult) {
        return new PaginationDTO(
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalItems(),
                pageResult.getTotalPages(),
                pageResult.isHasNext(),
                pageResult.isHasPrevious()
        );
    }


    default PageResultDTO<InvoiceResponseDTO> toPageResultInvoiceDTO(PageResult<Invoice> pageResult) {
        return new PageResultDTO<>(
                pageResult.getItems().stream()
                        .map(this::response)
                        .toList(),
                toPaginationDTO(pageResult)
        );
    }

}