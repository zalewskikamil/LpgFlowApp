CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE address
(
    id          BIGSERIAL PRIMARY KEY,
    uuid        UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version     BIGINT DEFAULT 0 NOT NULL,
    street      VARCHAR(150) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    postal_code VARCHAR(6) NOT NULL
);