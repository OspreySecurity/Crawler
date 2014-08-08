SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `OspreySecurity`
--
CREATE DATABASE IF NOT EXISTS `OspreySecurity` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `OspreySecurity`;

-- --------------------------------------------------------

--
-- Table structure for table `Domain`
--

DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `domain_name` varchar(40) NOT NULL,
  `crawl` tinyint(1) NOT NULL DEFAULT '1',
  `score` tinyint(3) unsigned NOT NULL,
  `times_visited` int(10) unsigned NOT NULL DEFAULT '0',
  `created_on` date NOT NULL,
  `last_crawled_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `domain_name` (`domain_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `domain_info`
--

DROP TABLE IF EXISTS `domain_info`;
CREATE TABLE `domain_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `domain_id` int(10) unsigned NOT NULL,
  `info_id` int(10) unsigned NOT NULL,
  `created_on` date NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `info_id` (`info_id`),
  KEY `domain_id` (`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `domain_port`
--

DROP TABLE IF EXISTS `domain_port`;
CREATE TABLE `domain_port` (
  `id` int(10) unsigned NOT NULL,
  `domain_id` int(10) unsigned NOT NULL,
  `port_id` int(10) unsigned NOT NULL,
  `is_open` tinyint(1) NOT NULL,
  `created_on` date NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `port_id` (`port_id`),
  KEY `domain_id` (`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `info`
--

DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `header_field` varchar(30) NOT NULL,
  `value` varchar(30) NOT NULL,
  `created_on` date NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `port`
--

DROP TABLE IF EXISTS `port`;
CREATE TABLE `port` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `port_number` int(10) unsigned NOT NULL,
  `description` varchar(30) NOT NULL COMMENT 'html, ftp, ssh, etc',
  `scan` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'wether or not to scan the port',
  `created_on` date NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `domain_info`
--
ALTER TABLE `domain_info`
  ADD CONSTRAINT `fk_di_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `Domain` (`id`),
  ADD CONSTRAINT `fk_di_info_id` FOREIGN KEY (`info_id`) REFERENCES `info` (`id`);

--
-- Constraints for table `domain_port`
--
ALTER TABLE `domain_port`
  ADD CONSTRAINT `fk_dp_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `Domain` (`id`),
  ADD CONSTRAINT `fk_dp_port_id` FOREIGN KEY (`port_id`) REFERENCES `port` (`id`);
