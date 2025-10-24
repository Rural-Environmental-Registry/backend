-- V8__create_user.sql

---------------------------------------------
-- Table: user
---------------------------------------------
CREATE TABLE users (
    id              UUID         NOT NULL,
    id_keycloak     VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    id_national     VARCHAR(255) NOT NULL,
    email           VARCHAR(255),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_id_keycloak UNIQUE (id_keycloak),
    CONSTRAINT uq_users_id_national UNIQUE (id_national)
);


