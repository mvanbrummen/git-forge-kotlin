CREATE TABLE "group" (
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    description VARCHAR,
    is_private BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() at time zone 'utc')
);

CREATE TABLE group_membership (
    id SERIAL PRIMARY KEY,
    group_id INTEGER NOT NULL,
    account_id UUID NOT NULL,
    CONSTRAINT group_membership_group_id_fk FOREIGN KEY (group_id) REFERENCES "group"(id),
    CONSTRAINT group_membership_account_id_fk FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

INSERT INTO role (name)
VALUES ('ADMINISTRATOR');

INSERT INTO role (name)
VALUES ('DEVELOPER');

INSERT INTO role (name)
VALUES ('GUEST');

CREATE TABLE group_membership_role (
    group_membership_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    UNIQUE (group_membership_id, role_id),
    CONSTRAINT group_membership_role_group_membership_id_fk FOREIGN KEY (group_membership_id) REFERENCES group_membership(id),
    CONSTRAINT group_membership_role_role_id_fk FOREIGN KEY (role_id) REFERENCES role(id)
);