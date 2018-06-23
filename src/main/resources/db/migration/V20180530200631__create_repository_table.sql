CREATE TABLE repository (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR,
    created_at timestamp default now(),
    is_private BOOLEAN default false,
    CONSTRAINT repository_account_id_fk FOREIGN KEY (account_id) REFERENCES account(id)
);