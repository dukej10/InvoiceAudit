package co.com.management.api.dto.mapper;

import co.com.management.api.dto.request.ClientDTO;
import co.com.management.api.dto.request.InvoiceDTO;
import co.com.management.api.dto.request.ProductDTO;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RQMapper {

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Client toModel(ClientDTO clientDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Invoice toModel(InvoiceDTO invoiceDTO);

    @Mapping(target = "id", ignore = true)
    Product toModel(ProductDTO productDTO);

}