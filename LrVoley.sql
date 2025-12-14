-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 18-11-2025 a las 11:05:24
-- Versión del servidor: 5.7.35-0ubuntu0.18.04.2
-- Versión de PHP: 8.0.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `LrVoley`
--
CREATE DATABASE IF NOT EXISTS `LrVoley` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `LrVoley`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Ejercicio`
--

DROP TABLE IF EXISTS `Ejercicio`;
CREATE TABLE `Ejercicio` (
  `id_ejer` int(11) NOT NULL,
  `nombre_ejer` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `finalidad` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `Ejercicio`
--

INSERT INTO `Ejercicio` (`id_ejer`, `nombre_ejer`, `tipo`, `finalidad`) VALUES
(1, 'Recepción básica', 'Recepción', 'Mejorar la técnica de recepción de balón'),
(2, 'Saque flotante', 'Saque', 'Perfeccionar el saque flotante'),
(3, 'Bloqueo individual', 'Bloqueo', 'Fortalecer la técnica de bloqueo'),
(4, 'Remate potente', 'Remate', 'Aumentar la potencia en el remate'),
(5, 'Defensa de campo', 'Defensa', 'Mejorar los reflejos defensivos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Entrenamientos`
--

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

--
-- Volcado de datos para la tabla `Entrenamientos`
--

INSERT INTO `Entrenamientos` (`id_entreno`, `id_usuario`, `id_ejer`, `nombre_entreno`, `repeticiones`, `fallos`, `aciertos`, `completado`) VALUES
(1, 1, 1, 'Entreno Recepción Mañana', 20, 3, 17, 1),
(2, 1, 2, 'Práctica Saque', 15, 5, 10, 0),
(3, 2, 3, 'Sesión Bloqueo', 25, 2, 23, 1),
(4, 3, 4, 'Entrenamiento Remate', 30, 8, 22, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuario`
--

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

--
-- Volcado de datos para la tabla `Usuario`
--

INSERT INTO `Usuario` (`id_usuario`, `nombre`, `apellido`, `login`, `contraseña`, `rol`, `correo`) VALUES
(1, 'Juan', 'Pérez', 'jperez', 'password123', 'usuario', 'jperez@email.com'),
(2, 'María', 'García', 'mgarcia', 'admin123', 'administrador', 'mgarcia@email.com'),
(3, 'Carlos', 'López', 'clopez', 'user456', 'usuario', 'clopez@email.com');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Ejercicio`
--
ALTER TABLE `Ejercicio`
  ADD PRIMARY KEY (`id_ejer`);

--
-- Indices de la tabla `Entrenamientos`
--
ALTER TABLE `Entrenamientos`
  ADD PRIMARY KEY (`id_entreno`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_ejer` (`id_ejer`);

--
-- Indices de la tabla `Usuario`
--
ALTER TABLE `Usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `login` (`login`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Ejercicio`
--
ALTER TABLE `Ejercicio`
  MODIFY `id_ejer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `Entrenamientos`
--
ALTER TABLE `Entrenamientos`
  MODIFY `id_entreno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `Usuario`
--
ALTER TABLE `Usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Entrenamientos`
--
ALTER TABLE `Entrenamientos`
  ADD CONSTRAINT `Entrenamientos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `Entrenamientos_ibfk_2` FOREIGN KEY (`id_ejer`) REFERENCES `Ejercicio` (`id_ejer`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
