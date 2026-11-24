CREATE DATABASE IF NOT EXISTS maquinaria_fx;
USE maquinaria_fx;

CREATE TABLE IF NOT EXISTS excavadora (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    horas_uso INT NOT NULL,
    potencia DOUBLE NOT NULL,
    peso_toneladas DOUBLE NOT NULL,
    mantencion_al_dia BOOLEAN NOT NULL,
    certificacion_seguridad BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS cargador_frontal (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    horas_uso INT NOT NULL,
    potencia DOUBLE NOT NULL,
    capacidad_balde DOUBLE NOT NULL
);

INSERT INTO excavadora (codigo, horas_uso, potencia, peso_toneladas, mantencion_al_dia, certificacion_seguridad)
SELECT 'EXC-101', 1200, 180.0, 22.5, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM excavadora WHERE codigo = 'EXC-101');

INSERT INTO excavadora (codigo, horas_uso, potencia, peso_toneladas, mantencion_al_dia, certificacion_seguridad)
SELECT 'EXC-102', 3400, 210.0, 28.0, FALSE, FALSE
WHERE NOT EXISTS (SELECT 1 FROM excavadora WHERE codigo = 'EXC-102');

INSERT INTO cargador_frontal (codigo, horas_uso, potencia, capacidad_balde)
SELECT 'CAR-201', 950, 160.0, 2.8
WHERE NOT EXISTS (SELECT 1 FROM cargador_frontal WHERE codigo = 'CAR-201');

INSERT INTO cargador_frontal (codigo, horas_uso, potencia, capacidad_balde)
SELECT 'CAR-202', 2100, 195.0, 3.6
WHERE NOT EXISTS (SELECT 1 FROM cargador_frontal WHERE codigo = 'CAR-202');
