use lt_domains;

drop table if exists related;
drop table if exists domains;

CREATE TABLE domains (
    id VARCHAR(36) PRIMARY KEY,
    status BOOLEAN DEFAULT TRUE,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by_user VARCHAR(36) NOT NULL,
    modification_date TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modified_by_user VARCHAR(36),
    -- company_id VARCHAR(36) NOT NULL,
    version NUMERIC DEFAULT 0
);

CREATE INDEX idx_domain_status ON domains (id, status); --company_id

CREATE TABLE related (
    id VARCHAR(36) PRIMARY KEY,
    domain_id VARCHAR(36) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by_user VARCHAR(36) NOT NULL,
    modification_date TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modified_by_user VARCHAR(36),
    -- company_id VARCHAR(36) NOT NULL,
    version NUMERIC DEFAULT 0,
    UNIQUE (domain_id, id)
);

ALTER TABLE related
    ADD CONSTRAINT fk_domain
    FOREIGN KEY (domain_id)
    REFERENCES domains(id)
    ON DELETE CASCADE;