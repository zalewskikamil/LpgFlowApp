CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE bdf
(
    id                                    BIGSERIAL PRIMARY KEY,
    uuid                                  UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on                            TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version                               BIGINT DEFAULT 0 NOT NULL,
    size                                  VARCHAR(50),
    ordered                               BOOLEAN      NOT NULL,
    created_by                            VARCHAR(254) NOT NULL
);