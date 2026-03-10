-- Configuración inicial
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- =========================
-- TABLAS
-- =========================

-- Tabla Actualizaciones
CREATE TABLE `Actualizaciones` (
  `Id_Actualizacion` int(11) NOT NULL,
  `Version` varchar(50) NOT NULL,
  `Fecha` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla Plantas
CREATE TABLE `Plantas` (
  `Id_Planta` int(11) NOT NULL,
  `Nombre_Comun` varchar(100) NOT NULL,
  `Nombre_Cientifico` varchar(100) NOT NULL,
  `Temperatura_Min` int(11) NOT NULL,
  `Temperatura_Max` int(11) NOT NULL,
  `Humedad` int(11) NOT NULL,
  `Cantidad_Agua` int(11) NOT NULL,
  `Frecuencia_Riego` int(11) NOT NULL,
  `Tipo_Riego` enum('Raiz','Pulverizar hojas') NOT NULL,
  `Estado` tinyint(1) NOT NULL,
  `Imagen` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================
-- DATOS INICIALES
-- =========================

INSERT INTO `Actualizaciones` (`Id_Actualizacion`, `Version`, `Fecha`) VALUES
(1, '1.0.1', '2026-03-08 15:32:58');

INSERT INTO `Plantas` (`Id_Planta`, `Nombre_Comun`, `Nombre_Cientifico`, `Temperatura_Min`, `Temperatura_Max`, `Humedad`, `Cantidad_Agua`, `Frecuencia_Riego`, `Tipo_Riego`, `Estado`, `Imagen`) VALUES
(1, 'Tomate', 'Solanum lycopersicum', 18, 30, 65, 500, 2, 'Raiz', 1, 'imagenes/1.jpg'),
(2, 'Clavel', 'Dianthus caryophyllus', 10, 25, 50, 200, 3, 'Raiz', 1, 'imagenes/2.jpg'),
(3, 'Rosa', 'Rosa spp.', 15, 28, 60, 400, 3, 'Raiz', 1, 'imagenes/3.jpg'),
(4, 'Zanahoria', 'Daucus carota', 10, 24, 70, 250, 2, 'Raiz', 1, 'imagenes/4.jpg'),
(5, 'Peregil', 'Petroselinum crispum', 12, 26, 75, 150, 2, 'Pulverizar hojas', 1, 'imagenes/5.jpg');

-- =========================
-- INDICES Y AUTO_INCREMENT
-- =========================

ALTER TABLE `Actualizaciones`
  ADD PRIMARY KEY (`Id_Actualizacion`);

ALTER TABLE `Plantas`
  ADD PRIMARY KEY (`Id_Planta`),
  ADD KEY `idx_estado_nombre` (`Estado`,`Nombre_Comun`),
  ADD KEY `idx_estado_cientifico` (`Estado`,`Nombre_Cientifico`);

ALTER TABLE `Actualizaciones`
  MODIFY `Id_Actualizacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

ALTER TABLE `Plantas`
  MODIFY `Id_Planta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

COMMIT;

-- =========================
-- PROCEDIMIENTOS ALMACENADOS
-- =========================

DELIMITER $$

-- Buscar plantas por ID o texto
CREATE PROCEDURE sp_buscar_plantas(IN texto VARCHAR(100))
BEGIN
    SELECT
        Id_Planta,
        Nombre_Comun,
        Nombre_Cientifico,
        Temperatura_Min,
        Temperatura_Max,
        Humedad,
        Cantidad_Agua,
        Frecuencia_Riego,
        Tipo_Riego,
        Imagen
    FROM Plantas
    WHERE Estado = 1
    AND (
        (texto REGEXP '^[0-9]+$' AND Id_Planta = texto)
        OR Nombre_Comun COLLATE utf8mb4_unicode_ci LIKE CONCAT('%',texto,'%')
        OR Nombre_Cientifico COLLATE utf8mb4_unicode_ci LIKE CONCAT('%',texto,'%')
    )
    ORDER BY Nombre_Comun;
END$$

-- Obtener última versión de Actualizaciones
CREATE PROCEDURE sp_actualizaciones()
BEGIN
    SELECT Version, Fecha
    FROM Actualizaciones
    ORDER BY Id_Actualizacion DESC
    LIMIT 1;
END$$

DELIMITER ;

-- =========================
-- TRIGGERS
-- =========================

DELIMITER $$

-- Trigger AFTER INSERT
CREATE TRIGGER tr_plantas_insert
AFTER INSERT ON Plantas
FOR EACH ROW
BEGIN
    UPDATE Actualizaciones
    SET Version = CONCAT(
        SUBSTRING_INDEX(Version,'.',2),
        '.',
        SUBSTRING_INDEX(Version,'.',-1) + 1
    ),
    Fecha = CURRENT_TIMESTAMP
    ORDER BY Id_Actualizacion DESC
    LIMIT 1;
END$$

-- Trigger AFTER UPDATE
CREATE TRIGGER tr_plantas_update
AFTER UPDATE ON Plantas
FOR EACH ROW
BEGIN
    UPDATE Actualizaciones
    SET Version = CONCAT(
        SUBSTRING_INDEX(Version,'.',2),
        '.',
        SUBSTRING_INDEX(Version,'.',-1) + 1
    ),
    Fecha = CURRENT_TIMESTAMP
    ORDER BY Id_Actualizacion DESC
    LIMIT 1;
END$$

-- Trigger BEFORE DELETE → soft delete
CREATE TRIGGER tr_plantas_delete
BEFORE DELETE ON Plantas
FOR EACH ROW
BEGIN
    -- Soft delete
    UPDATE Plantas
    SET Estado = 0
    WHERE Id_Planta = OLD.Id_Planta;

    -- Incrementar versión
    UPDATE Actualizaciones
    SET Version = CONCAT(
        SUBSTRING_INDEX(Version,'.',2),
        '.',
        SUBSTRING_INDEX(Version,'.',-1) + 1
    ),
    Fecha = CURRENT_TIMESTAMP
    ORDER BY Id_Actualizacion DESC
    LIMIT 1;

    -- Cancelar DELETE real
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Baja logica aplicada';
END$$

DELIMITER ;

-- =========================
-- COMMIT FINAL
-- =========================

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;