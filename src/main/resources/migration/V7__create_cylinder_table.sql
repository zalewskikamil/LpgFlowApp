CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cylinder
(
    id              BIGSERIAL PRIMARY KEY,
    uuid            UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version         BIGINT DEFAULT 0 NOT NULL,
    capacity        VARCHAR(50),
    gas_type        VARCHAR(50),
    usage_type      VARCHAR(50),
    has_collar      BOOLEAN NOT NULL,
    additional_info VARCHAR(255),
    bdf_slots       INTEGER NOT NULL
);