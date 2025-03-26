CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE bdf_cylinder
(
    uuid        UUID DEFAULT uuid_generate_v4() NOT NULL UNIQUE,
    created_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT now(),
    version     BIGINT DEFAULT 0 NOT NULL,
    quantity    INTEGER NOT NULL,
    bdf_id      BIGINT  NOT NULL,
    cylinder_id BIGINT  NOT NULL,
    CONSTRAINT pk_bdfcylinder PRIMARY KEY (bdf_id, cylinder_id)
);

ALTER TABLE bdf_cylinder
    ADD CONSTRAINT FK_BDFCYLINDER_ON_BDF FOREIGN KEY (bdf_id) REFERENCES bdf (id);

ALTER TABLE bdf_cylinder
    ADD CONSTRAINT FK_BDFCYLINDER_ON_CYLINDER FOREIGN KEY (cylinder_id) REFERENCES cylinder (id);