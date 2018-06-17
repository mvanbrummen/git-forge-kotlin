CREATE TABLE repository (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() at time zone 'utc'),
    is_private BOOLEAN DEFAULT false,
    CONSTRAINT repository_account_id_fk FOREIGN KEY (account_id) REFERENCES account(id)
);