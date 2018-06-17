CREATE TABLE account (
    id UUID PRIMARY KEY,
    username VARCHAR UNIQUE NOT NULL,
    email_address VARCHAR UNIQUE NOT NULL,
    full_name VARCHAR,
    description VARCHAR,
    password VARCHAR NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() at time zone 'utc')
);