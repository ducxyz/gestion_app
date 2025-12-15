-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2025 at 11:24 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestion_appartements`
--

-- --------------------------------------------------------

--
-- Table structure for table `appartement`
--

CREATE TABLE `appartement` (
  `id_appartement` int(11) NOT NULL,
  `numero` varchar(10) NOT NULL,
  `surface` double NOT NULL,
  `statut` varchar(20) DEFAULT 'LIBRE',
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appartement`
--

INSERT INTO `appartement` (`id_appartement`, `numero`, `surface`, `statut`, `description`) VALUES
(1, 'A101', 35.5, 'LOUE', 'Studio lumineux, rez-de-chaussée'),
(2, 'A102', 50, 'LIBRE', 'T2 spacieux avec balcon'),
(3, 'B201', 45, 'LOUE', 'Appartement rénové, vue sur parc'),
(4, 'B202', 65.5, 'LIBRE', 'Grand T3, idéal pour famille'),
(5, 'C301', 28, 'LOUE', 'Studio étudiant, proche université'),
(6, 'C302', 40, 'LIBRE', 'T2 moderne, cuisine équipée'),
(7, 'D401', 80, 'LOUE', 'Penthouse avec terrasse'),
(8, 'D402', 55, 'LOUE', 'Appartement calme, dernier étage');

-- --------------------------------------------------------

--
-- Table structure for table `compte`
--

CREATE TABLE `compte` (
  `id_compte` int(11) NOT NULL,
  `nom_utilisateur` varchar(50) NOT NULL,
  `mot_de_passe_hash` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `statut` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `compte`
--

INSERT INTO `compte` (`id_compte`, `nom_utilisateur`, `mot_de_passe_hash`, `role`, `statut`) VALUES
(1, 'admin', '123', 'ADMIN', 1),
(2, 'ductrungbui06', '123', 'USER', 1),
(3, 'user1', '1', 'USER', 1);

-- --------------------------------------------------------

--
-- Table structure for table `contratlocation`
--

CREATE TABLE `contratlocation` (
  `id_contrat` int(11) NOT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `loyer_mensuel` double DEFAULT NULL,
  `statut` varchar(20) DEFAULT NULL,
  `id_appartement` int(11) DEFAULT NULL,
  `id_resident` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `contratlocation`
--

INSERT INTO `contratlocation` (`id_contrat`, `date_debut`, `date_fin`, `loyer_mensuel`, `statut`, `id_appartement`, `id_resident`) VALUES
(1, '2025-12-01', '2026-01-01', 500, 'EN_COURS', 1, 1),
(2, '2025-12-01', '2026-12-01', 5000, 'EN_COURS', 3, 4),
(3, '2025-12-01', '2028-12-01', 30000, 'EN_COURS', 4, 7),
(4, '2025-12-01', '2030-12-15', 50000, 'EN_COURS', 8, 7),
(5, '2024-02-03', '2026-04-15', 600, 'TERMINE', 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `resident`
--

CREATE TABLE `resident` (
  `id_resident` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `numero_identite` varchar(20) NOT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `id_compte` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resident`
--

INSERT INTO `resident` (`id_resident`, `nom`, `prenom`, `numero_identite`, `telephone`, `date_naissance`, `id_compte`) VALUES
(1, 'Dupont', 'Jean', 'FR123456', '0601020304', '1990-05-15', 3),
(2, 'Martin', 'Sophie', 'FR654321', '0612345678', '1985-11-22', 0),
(3, 'Bernard', 'Lucas', 'FR112233', '0698765432', '1995-03-10', 0),
(4, 'Dubois', 'Emma', 'FR443322', '0655443322', '1992-07-30', 0),
(5, 'Leroy', 'Thomas', 'FR998877', '0677889900', '1988-12-05', 0),
(6, 'Moreau', 'Camille', 'FR776655', '0644556677', '1998-09-12', 0),
(7, 'BUI', 'Trung Duc', '088888888', '0667997674', '2004-01-06', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `appartement`
--
ALTER TABLE `appartement`
  ADD PRIMARY KEY (`id_appartement`);

--
-- Indexes for table `compte`
--
ALTER TABLE `compte`
  ADD PRIMARY KEY (`id_compte`),
  ADD UNIQUE KEY `nom_utilisateur` (`nom_utilisateur`);

--
-- Indexes for table `contratlocation`
--
ALTER TABLE `contratlocation`
  ADD PRIMARY KEY (`id_contrat`),
  ADD KEY `id_appartement` (`id_appartement`),
  ADD KEY `id_resident` (`id_resident`);

--
-- Indexes for table `resident`
--
ALTER TABLE `resident`
  ADD PRIMARY KEY (`id_resident`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `appartement`
--
ALTER TABLE `appartement`
  MODIFY `id_appartement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `compte`
--
ALTER TABLE `compte`
  MODIFY `id_compte` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `contratlocation`
--
ALTER TABLE `contratlocation`
  MODIFY `id_contrat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `resident`
--
ALTER TABLE `resident`
  MODIFY `id_resident` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `contratlocation`
--
ALTER TABLE `contratlocation`
  ADD CONSTRAINT `contratlocation_ibfk_1` FOREIGN KEY (`id_appartement`) REFERENCES `appartement` (`id_appartement`),
  ADD CONSTRAINT `contratlocation_ibfk_2` FOREIGN KEY (`id_resident`) REFERENCES `resident` (`id_resident`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
