CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    uuid         UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version      BIGINT DEFAULT 0 NOT NULL,
    name         VARCHAR(50) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    email        VARCHAR(254) NOT NULL UNIQUE,
    password     VARCHAR(255),
    phone_number VARCHAR(9),
    enabled      BOOLEAN NOT NULL DEFAULT TRUE,
    blocked      BOOLEAN NOT NULL DEFAULT FALSE
);