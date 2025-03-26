CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE warehouse
(
    id                                   BIGSERIAL PRIMARY KEY,
    uuid                                 UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on                           TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version                              BIGINT DEFAULT 0 NOT NULL,
    name                                 VARCHAR(15) NOT NULL UNIQUE,
    regional_manager_email               VARCHAR(254) NOT NULL,
    warehouseman_email                   VARCHAR(254) NOT NULL,
    address_id                           BIGINT,
    active                               BOOLEAN NOT NULL DEFAULT FALSE,
    bdf_size                             VARCHAR(15),
    max_cylinders_without_collar_per_bdf INTEGER      NOT NULL
);