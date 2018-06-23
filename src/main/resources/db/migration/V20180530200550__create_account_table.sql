CREATE TABLE account (
    id UUID PRIMARY KEY,
    username VARCHAR NOT NULL,
    email_address VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    created_at timestamp default now()
);