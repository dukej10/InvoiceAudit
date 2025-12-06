package co.com.management.r2dbc.persistence.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name="products")
public class ProductEntity {

    @Id
    private UUID id;

    @Column("name_product")
    private String name;

    private Integer quantity;

    @Column("unit_price")
    private BigDecimal unitPrice;

    @Column("invoice_id")
    private UUID invoiceId;
}




