CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE orders
(
    id              BIGSERIAL PRIMARY KEY,
    uuid            UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version         BIGINT DEFAULT 0 NOT NULL,
    created_by      VARCHAR(254) NOT NULL,
    bdf_ids         VARCHAR(255) NOT NULL,
    completion_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    warehouse_name  VARCHAR(15) NOT NULL,
    status          VARCHAR(32)
);