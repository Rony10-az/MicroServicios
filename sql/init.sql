-- Script de inicializacion ejecutado automaticamente por el contenedor MySQL
-- (docker-entrypoint-initdb.d). Crea una base de datos independiente por
-- microservicio, cumpliendo el requisito "cada microservicio tiene su propia
-- base de datos, sin compartir tablas".

CREATE DATABASE IF NOT EXISTS db_clientes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_servicios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS db_reservas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Los microservicios usan Hibernate (spring.jpa.hibernate.ddl-auto=update) para
-- crear sus propias tablas al arrancar. A continuacion se dejan sentencias de
-- referencia (comentadas) por si se requiere crear el esquema manualmente:

-- USE db_clientes;
-- CREATE TABLE clientes (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     nombre VARCHAR(100) NOT NULL,
--     apellido VARCHAR(100) NOT NULL,
--     email VARCHAR(150) NOT NULL UNIQUE,
--     telefono VARCHAR(20),
--     direccion VARCHAR(200),
--     fecha_registro DATETIME
-- );

-- USE db_servicios;
-- CREATE TABLE servicios (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     nombre VARCHAR(120) NOT NULL,
--     descripcion VARCHAR(300),
--     duracion_minutos INT NOT NULL,
--     precio DECIMAL(10,2) NOT NULL
-- );
-- CREATE TABLE horarios (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     servicio_id BIGINT NOT NULL,
--     fecha DATE NOT NULL,
--     hora_inicio TIME NOT NULL,
--     hora_fin TIME NOT NULL,
--     disponible BOOLEAN NOT NULL DEFAULT TRUE,
--     FOREIGN KEY (servicio_id) REFERENCES servicios(id)
-- );

-- USE db_reservas;
-- CREATE TABLE reservas (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     cliente_id BIGINT NOT NULL,
--     servicio_id BIGINT NOT NULL,
--     horario_id BIGINT NOT NULL,
--     estado VARCHAR(20) NOT NULL,
--     fecha_creacion DATETIME,
--     fecha_actualizacion DATETIME
-- );
