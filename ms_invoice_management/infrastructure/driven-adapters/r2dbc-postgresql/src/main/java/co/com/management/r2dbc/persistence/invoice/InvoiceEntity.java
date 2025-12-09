package co.com.management.r2dbc.persistence.invoice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name="invoices")
public class InvoiceEntity {

    @Id
    private UUID id;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("client_id")
    private UUID clientId;

    @Column("created_at")
    private LocalDateTime createdDate;

}
