package co.com.management.r2dbc.persistence.invoice;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
@NoArgsConstructor
@Table(name="products")
public class ProductEntity {

    @Id
    private String id;

    @Column("name_product")
    private String name;

    private Integer quantity;

    @Column("unit_price")
    private Float unitPrice;

    @Transient
    @Column("invoice_id")
    private String invoiceId;
}
