-- Crear schema si no existe
CREATE SCHEMA IF NOT EXISTS makers;

-- Usar schema
USE makers;

-- Borrar tablas si ya existen (en orden por las FK)
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS loan_status;

-- Crear tabla de estados
CREATE TABLE loan_status (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Crear tabla de prestamos
CREATE TABLE loans (
    id VARCHAR(36) PRIMARY KEY,
    amount DECIMAL(12,2) NOT NULL,
    term INT NOT NULL, -- plazo días
    status_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES loan_status(id)
);

INSERT INTO loan_status (id, name) VALUES
('11111111-1111-1111-1111-111111111111', 'PENDIENTE'),
('22222222-2222-2222-2222-222222222222', 'APROBADO'),
('33333333-3333-3333-3333-333333333333', 'RECHAZADO');
