CREATE TABLE clients (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_number  VARCHAR(20)   NOT NULL,
    document_type    VARCHAR(2)    NOT NULL,
    first_name       VARCHAR(50)   NOT NULL,
    last_name        VARCHAR(50)   NOT NULL,
    state            BOOLEAN       NOT NULL DEFAULT true,
    email            VARCHAR(100)  NOT NULL,
    phone            VARCHAR(20)   NOT NULL,
    address          VARCHAR(150)  NOT NULL,
    created_at       TIMESTAMPTZ,
    updated_at       TIMESTAMPTZ,

    CONSTRAINT uq_client_document UNIQUE (document_number, document_type)
);

CREATE INDEX ix_clients_email ON clients(email);
CREATE INDEX ix_clients_state ON clients(state);

CREATE TABLE invoices (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_amount  NUMERIC(14,2) NOT NULL CHECK (total_amount >= 0),
    client_id     UUID NOT NULL,
    created_at    TIMESTAMPTZ,

    CONSTRAINT fk_invoice_client 
        FOREIGN KEY (client_id) REFERENCES clients(id) 
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX ix_invoices_client_id ON invoices(client_id);
CREATE INDEX ix_invoices_created_at ON invoices(created_at DESC);

CREATE TABLE products (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name_product VARCHAR(200) NOT NULL,
    quantity    INTEGER NOT NULL DEFAULT 1 CHECK (quantity >= 0),
    unit_price  NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
    invoice_id  UUID NOT NULL,

    CONSTRAINT fk_product_invoice 
        FOREIGN KEY (invoice_id) REFERENCES invoices(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX ix_products_invoice_id ON products(invoice_id);
CREATE INDEX ix_products_name ON products(name_product);