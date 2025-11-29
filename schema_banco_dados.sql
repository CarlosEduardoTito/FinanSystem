SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

DROP TABLE IF EXISTS `transacao`;
DROP TABLE IF EXISTS `meta_financeira`;
DROP TABLE IF EXISTS `categoria`;
DROP TABLE IF EXISTS `conta`;
DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome_completo` VARCHAR(150) NOT NULL,
  `data_nascimento` DATE NOT NULL,
  `sexo` CHAR(1) NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `conta` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nomeBanco` VARCHAR(100) NOT NULL,
  `agencia` VARCHAR(20) NOT NULL,
  `numeroConta` INT(11) NOT NULL,
  `saldoInicial` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `tipoConta` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `categoria` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `tipo` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `transacao` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `idConta` INT(11) NOT NULL,
  `idCategoria` INT(11) NOT NULL,
  `descricao` VARCHAR(255) NOT NULL,
  `valor` DECIMAL(15,2) NOT NULL,
  `data` DATE NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_conta` (`idConta`),
  KEY `idx_categoria` (`idCategoria`),
  KEY `idx_data` (`data`),
  CONSTRAINT `fk_transacao_conta` FOREIGN KEY (`idConta`) 
    REFERENCES `conta` (`id`) 
    ON DELETE RESTRICT 
    ON UPDATE CASCADE,
  CONSTRAINT `fk_transacao_categoria` FOREIGN KEY (`idCategoria`) 
    REFERENCES `categoria` (`id`) 
    ON DELETE RESTRICT 
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `meta_financeira` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `usuarioId` INT(11) NOT NULL,
  `descricao` VARCHAR(255) NOT NULL,
  `valorMensal` DECIMAL(10,2) NOT NULL,
  `tipoMeta` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_usuario` (`usuarioId`),
  CONSTRAINT `fk_meta_usuario` FOREIGN KEY (`usuarioId`) 
    REFERENCES `usuario` (`id`) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
