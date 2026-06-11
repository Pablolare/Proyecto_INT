SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `LrVoley` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `LrVoley`;

-- Tabla Usuario
DROP TABLE IF EXISTS `Usuario`;
CREATE TABLE `Usuario` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `login` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contraseña` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Usuario` (`id_usuario`, `nombre`, `apellido`, `login`, `contraseña`, `rol`, `correo`) VALUES
(1, 'Juan', 'Pérez', 'jperez', 'password123', 'usuario', 'jperez@email.com'),
(2, 'María', 'García', 'mgarcia', 'admin123', 'administrador', 'mgarcia@email.com'),
(3, 'Carlos', 'López', 'clopez', 'user456', 'usuario', 'clopez@email.com');

-- Tabla Ejercicio
DROP TABLE IF EXISTS `Ejercicio`;
CREATE TABLE `Ejercicio` (
  `id_ejer` int(11) NOT NULL,
  `nombre_ejer` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `finalidad` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Ejercicio` (`id_ejer`, `nombre_ejer`, `tipo`, `finalidad`) VALUES
(1, 'Recepción básica', 'Recepción', 'Mejorar la técnica de recepción de balón'),
(2, 'Saque flotante', 'Saque', 'Perfeccionar el saque flotante'),
(3, 'Bloqueo individual', 'Bloqueo', 'Fortalecer la técnica de bloqueo'),
(4, 'Remate potente', 'Remate', 'Aumentar la potencia en el remate'),
(5, 'Defensa de campo', 'Defensa', 'Mejorar los reflejos defensivos');

-- Tabla Entrenamientos
DROP TABLE IF EXISTS `Entrenamientos`;
CREATE TABLE `Entrenamientos` (
  `id_entreno` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_ejer` int(11) NOT NULL,
  `nombre_entreno` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `repeticiones` int(11) DEFAULT '0',
  `fallos` int(11) DEFAULT '0',
  `aciertos` int(11) DEFAULT '0',
  `completado` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Entrenamientos` (`id_entreno`, `id_usuario`, `id_ejer`, `nombre_entreno`, `repeticiones`, `fallos`, `aciertos`, `completado`) VALUES
(1, 1, 1, 'Entreno Recepción Mañana', 20, 3, 17, 1),
(2, 1, 2, 'Práctica Saque', 15, 5, 10, 0),
(3, 2, 3, 'Sesión Bloqueo', 25, 2, 23, 1),
(4, 3, 4, 'Entrenamiento Remate', 30, 8, 22, 0);

-- Tabla Categoria
DROP TABLE IF EXISTS `Categoria`;
CREATE TABLE `Categoria` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Categoria` (`id_categoria`, `nombre`, `descripcion`) VALUES
(1, 'Calentamiento', 'Ejercicios de preparación física antes del entrenamiento'),
(2, 'Técnica', 'Ejercicios enfocados en mejorar la técnica individual'),
(3, 'Táctica', 'Ejercicios de estrategia y juego en equipo'),
(4, 'Físico', 'Ejercicios de acondicionamiento físico y resistencia');

-- Tabla Ejercicio_Categoria (relacion N:M)
DROP TABLE IF EXISTS `Ejercicio_Categoria`;
CREATE TABLE `Ejercicio_Categoria` (
  `id_ejer` int(11) NOT NULL,
  `id_categoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Ejercicio_Categoria` (`id_ejer`, `id_categoria`) VALUES
(1, 2),
(2, 2),
(3, 2),
(3, 4),
(4, 2),
(4, 4),
(5, 2);

-- Tabla Nota
DROP TABLE IF EXISTS `Nota`;
CREATE TABLE `Nota` (
  `id_nota` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_entreno` int(11) DEFAULT NULL,
  `titulo` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contenido` text COLLATE utf8mb4_unicode_ci,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Nota` (`id_nota`, `id_usuario`, `id_entreno`, `titulo`, `contenido`, `fecha`) VALUES
(1, 1, 1, 'Buena sesión de recepción', 'He mejorado bastante la posición de los brazos en la recepción', '2025-11-10 10:30:00'),
(2, 1, 2, 'Saque por mejorar', 'Necesito trabajar más la dirección del saque flotante', '2025-11-11 12:00:00'),
(3, 2, 3, 'Bloqueo perfecto', 'Muy buena sesión, el timing del salto ha mejorado mucho', '2025-11-12 09:15:00'),
(4, 3, NULL, 'Objetivo semanal', 'Esta semana quiero completar al menos 3 entrenamientos de remate', '2025-11-13 08:00:00');

-- Tabla Objetivo
DROP TABLE IF EXISTS `Objetivo`;
CREATE TABLE `Objetivo` (
  `id_objetivo` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `meta_aciertos` int(11) DEFAULT NULL,
  `meta_repeticiones` int(11) DEFAULT NULL,
  `cumplido` tinyint(1) DEFAULT '0',
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Objetivo` (`id_objetivo`, `id_usuario`, `descripcion`, `meta_aciertos`, `meta_repeticiones`, `cumplido`, `fecha_creacion`) VALUES
(1, 1, 'Conseguir 50 aciertos en recepción', 50, NULL, 0, '2025-11-10 08:00:00'),
(2, 1, 'Completar 100 repeticiones de saque', NULL, 100, 0, '2025-11-10 08:00:00'),
(3, 2, 'Lograr 30 bloqueos exitosos', 30, NULL, 0, '2025-11-11 09:00:00'),
(4, 3, 'Alcanzar 80% de aciertos en remate', 40, 50, 0, '2025-11-12 10:00:00');

-- Indices
ALTER TABLE `Usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `login` (`login`),
  ADD UNIQUE KEY `correo` (`correo`);

ALTER TABLE `Ejercicio`
  ADD PRIMARY KEY (`id_ejer`);

ALTER TABLE `Entrenamientos`
  ADD PRIMARY KEY (`id_entreno`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_ejer` (`id_ejer`);

ALTER TABLE `Categoria`
  ADD PRIMARY KEY (`id_categoria`);

ALTER TABLE `Ejercicio_Categoria`
  ADD PRIMARY KEY (`id_ejer`, `id_categoria`),
  ADD KEY `id_categoria` (`id_categoria`);

ALTER TABLE `Nota`
  ADD PRIMARY KEY (`id_nota`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_entreno` (`id_entreno`);

ALTER TABLE `Objetivo`
  ADD PRIMARY KEY (`id_objetivo`),
  ADD KEY `id_usuario` (`id_usuario`);

-- AUTO_INCREMENT
ALTER TABLE `Usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `Ejercicio`
  MODIFY `id_ejer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `Entrenamientos`
  MODIFY `id_entreno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

ALTER TABLE `Categoria`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

ALTER TABLE `Nota`
  MODIFY `id_nota` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

ALTER TABLE `Objetivo`
  MODIFY `id_objetivo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

-- Foreign Keys
ALTER TABLE `Entrenamientos`
  ADD CONSTRAINT `Entrenamientos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `Entrenamientos_ibfk_2` FOREIGN KEY (`id_ejer`) REFERENCES `Ejercicio` (`id_ejer`) ON DELETE CASCADE;

ALTER TABLE `Ejercicio_Categoria`
  ADD CONSTRAINT `EjCat_ibfk_1` FOREIGN KEY (`id_ejer`) REFERENCES `Ejercicio` (`id_ejer`) ON DELETE CASCADE,
  ADD CONSTRAINT `EjCat_ibfk_2` FOREIGN KEY (`id_categoria`) REFERENCES `Categoria` (`id_categoria`) ON DELETE CASCADE;

ALTER TABLE `Nota`
  ADD CONSTRAINT `Nota_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `Nota_ibfk_2` FOREIGN KEY (`id_entreno`) REFERENCES `Entrenamientos` (`id_entreno`) ON DELETE SET NULL;

ALTER TABLE `Objetivo`
  ADD CONSTRAINT `Objetivo_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE;

COMMIT;
