-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 13, 2015 at 09:30 AM
-- Server version: 5.0.27-community-nt
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `gsoc`
--

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE IF NOT EXISTS `posts` (
  `id` int(10) NOT NULL auto_increment,
  `title` text,
  `vision` text,
  `timestamp` timestamp NULL default NULL,
  `email` text,
  `stori` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`id`, `title`, `vision`, `timestamp`, `email`, `stori`) VALUES
(5, '550128d4993088.72280366', 'pkay', '2015-03-12 05:49:08', 'x', NULL),
(6, '55012afac0f960.18933607', 'okay', '2015-03-12 05:58:18', 'x', NULL),
(7, '55012c626369a7.99671117', 'gdsss', '2015-03-12 06:04:18', 'x', NULL),
(8, '55012d23527ec5.73393426', 'okay', '2015-03-12 06:07:31', 'x', NULL),
(9, '550136577f1765.21535688', 'wadup', '2015-03-12 06:46:47', 'v', NULL),
(10, 'yea', 'thank god', '2015-03-12 22:32:37', 'x', NULL),
(11, 'yes', 'thankgod', '2015-03-12 23:32:58', 'osy', NULL),
(12, 'okay', 'yea', '2015-03-12 23:38:01', 'x', 'wadup'),
(13, 'hghj', 'okay', '2015-03-12 23:49:30', 'x', 'yea'),
(14, 'cghhh', 'dhhki', '2015-03-12 23:54:58', 'x', 'ddyuu'),
(15, 'gjmcc', 'dhjcvhth', '2015-03-13 00:02:11', 'x', 'dbbfhjh'),
(16, 'ositano', 'onyejinho', '2015-03-13 00:12:03', 'x', 'greeninho'),
(17, 'watecf', 'okgff', '2015-03-13 00:14:33', 'x', 'ddfgj');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
