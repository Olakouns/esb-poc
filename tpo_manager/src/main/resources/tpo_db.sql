-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 01, 2024 at 06:35 PM
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
-- Database: `tpo_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `constant_config`
--

CREATE TABLE `constant_config` (
  `id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `value_content` varchar(255) DEFAULT NULL,
  `wsdl_doc` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `constant_config`
--

INSERT INTO `constant_config` (`id`, `description`, `key_name`, `value_content`, `wsdl_doc`) VALUES
(1, NULL, 'IN', 'http://localhost:8091/ws', 'http://localhost:8091/ws/in_api.wsdl'),
(2, NULL, 'HLR', 'http://localhost:8092/ws', 'http://localhost:8092/ws/hlr_api.wsdl');

-- --------------------------------------------------------

--
-- Table structure for table `list_node`
--

CREATE TABLE `list_node` (
  `id` int(11) NOT NULL,
  `data_id` int(11) NOT NULL,
  `next_node_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `list_node`
--

INSERT INTO `list_node` (`id`, `data_id`, `next_node_id`) VALUES
(1, 7, NULL),
(2, 5, 1),
(3, 3, 2),
(4, 1, 3),
(5, 2, NULL),
(6, 2, NULL),
(7, 6, 6),
(10, 7, NULL),
(11, 5, 10),
(12, 2, NULL),
(13, 6, 12);

-- --------------------------------------------------------

--
-- Table structure for table `tpodata`
--

CREATE TABLE `tpodata` (
  `id` int(11) NOT NULL,
  `description` text DEFAULT NULL,
  `is_critical` bit(1) NOT NULL,
  `tpo` varchar(255) DEFAULT NULL,
  `tpo_condition` varchar(255) DEFAULT NULL,
  `verb` varchar(255) DEFAULT NULL,
  `list_node_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tpodata`
--

INSERT INTO `tpodata` (`id`, `description`, `is_critical`, `tpo`, `tpo_condition`, `verb`, `list_node_id`) VALUES
(1, '', b'0', 'TPO_CREATE_SUBSCRIBER_PRE_PAID', 'PRE_PAID', 'ADD_PRE_PAID', 4),
(2, '', b'0', 'TPO_DEACTIVATE_SUBS_HLR', 'PRE_PAID', 'DEACTIVATE_SUBS_HLR', 5),
(3, '', b'0', 'TPO_DEL_SUBS_HLR_IN_RECHARG_FAIL', 'PRE_PAID', 'DEL_SUBS_HLR_IN_RECHARG_FAIL', 7),
(4, 'Delete a pre paid subscriber', b'1', 'TPO_DELETE_SUBSCRIBER_PRE_PAID', 'PRE_PAID', 'DELETE_SUBSCRIBER_PRE_PAID', 13),
(5, '', b'0', 'TPO_DELETE_SUB_FAILURE', 'PRE_PAID', 'DELETE_SUB_FAILURE', 11);

-- --------------------------------------------------------

--
-- Table structure for table `tpodata_patterns`
--

CREATE TABLE `tpodata_patterns` (
  `tpodata_id` int(11) NOT NULL,
  `patterns_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tpodata_patterns`
--

INSERT INTO `tpodata_patterns` (`tpodata_id`, `patterns_id`) VALUES
(1, 1),
(1, 3),
(1, 5),
(1, 7),
(2, 2),
(3, 6),
(3, 2),
(5, 5),
(5, 7),
(4, 6),
(4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tpodata_previous_states_data`
--

CREATE TABLE `tpodata_previous_states_data` (
  `tpodata_id` int(11) NOT NULL,
  `previous_states_data_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tpodata_previous_states_data`
--

INSERT INTO `tpodata_previous_states_data` (`tpodata_id`, `previous_states_data_id`) VALUES
(4, 4),
(4, 8);

-- --------------------------------------------------------

--
-- Table structure for table `tpowork_order`
--

CREATE TABLE `tpowork_order` (
  `id` int(11) NOT NULL,
  `can_be_delete` bit(1) NOT NULL,
  `equipment` varchar(255) DEFAULT NULL,
  `is_service_template` bit(1) NOT NULL,
  `template` text DEFAULT NULL,
  `web_service_class_name` varchar(255) DEFAULT NULL,
  `web_service_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tpowork_order`
--

INSERT INTO `tpowork_order` (`id`, `can_be_delete`, `equipment`, `is_service_template`, `template`, `web_service_class_name`, `web_service_name`) VALUES
(1, b'0', 'HLR', b'0', '<activateSubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\">\r\n    <name>${subscriberName}</name>\r\n    <phoneNumber>${phoneNumber}</phoneNumber>\r\n    <imsi>${imsi}</imsi>\r\n    <subscriberType>${subscriberType}</subscriberType>\r\n</activateSubscriberRequest>', '', 'Activate Subscriber'),
(2, b'0', 'HLR', b'0', '<deactivateSubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\" phoneNumber=\"${phoneNumber}\" />', '', 'Deactivate Subscriber'),
(3, b'0', 'HLR', b'1', '<modifyServiceSubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\" phoneNumber=\"${phoneNumber}\" verb=\"ADD\">\r\n    <service>\r\n        <serviceType>${serviceType}</serviceType>\r\n        <targetNumber>${targetNumber}</targetNumber>\r\n        <active>${active}</active>\r\n    </service>\r\n</modifyServiceSubscriberRequest>', '', 'Modify Subscriber Service'),
(4, b'0', 'HLR', b'0', '<displaySubscriberRequest xmlns=\"http://esmt.sn/hlr_api/soam\" phoneNumber=\"${phoneNumber}\" />', 'displaySubscriberResponse', 'Display Subscriber'),
(5, b'0', 'IN', b'0', '<newConnectionRequest xmlns=\"http://esmt.sn/in_api/soam\">\r\n    <name>${subscriberName}</name>\r\n    <phoneNumber>${phoneNumber}</phoneNumber>\r\n    <imsi>${imsi}</imsi>\r\n</newConnectionRequest>', '', 'New Connection '),
(6, b'0', 'IN', b'0', ' <terminationRequest phoneNumber=\"${phoneNumber}\" xmlns=\"http://esmt.sn/in_api/soam\"/>', '', 'Termination Request'),
(7, b'0', 'IN', b'0', '<rechargingRequest xmlns=\"http://esmt.sn/in_api/soam\" phoneNumber=\"${phoneNumber}\">\r\n    <dataBalance>${dataBalance}</dataBalance>\r\n    <callBalance>${callBalance}</callBalance>\r\n    <smsBalance>${smsBalance}</smsBalance>\r\n</rechargingRequest>', '', 'Recharging Request'),
(8, b'0', 'IN', b'0', '<displaySubscriberRequest xmlns=\"http://esmt.sn/in_api/soam\" phoneNumber=\"${phoneNumber}\" />', 'displaySubscriberResponse', 'Display Subscriber');

-- --------------------------------------------------------

--
-- Table structure for table `tpo_failure_state`
--

CREATE TABLE `tpo_failure_state` (
  `id` int(11) NOT NULL,
  `tpo_failure_id` int(11) NOT NULL,
  `tpo_id` int(11) NOT NULL,
  `wo_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tpo_failure_state`
--

INSERT INTO `tpo_failure_state` (`id`, `tpo_failure_id`, `tpo_id`, `wo_id`) VALUES
(1, 2, 1, 5),
(2, 3, 1, 7);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `constant_config`
--
ALTER TABLE `constant_config`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_lr07sd0mq0oll73slbvo8lw23` (`key_name`);

--
-- Indexes for table `list_node`
--
ALTER TABLE `list_node`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKg2se4yakc6f3s6sl8h6njtbal` (`next_node_id`);

--
-- Indexes for table `tpodata`
--
ALTER TABLE `tpodata`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3jt471nxfofev66fblbmf21yb` (`list_node_id`);

--
-- Indexes for table `tpodata_patterns`
--
ALTER TABLE `tpodata_patterns`
  ADD KEY `FKj2sk3kvvaolcrev82xogci1k3` (`patterns_id`),
  ADD KEY `FKr2wakx7dnvhmp8xqu1x1kn58n` (`tpodata_id`);

--
-- Indexes for table `tpodata_previous_states_data`
--
ALTER TABLE `tpodata_previous_states_data`
  ADD UNIQUE KEY `UK_sr9cpmhblyslaa3v7n7s8vquh` (`previous_states_data_id`),
  ADD KEY `FKiy501r4679e5lh5bemx4qj4pe` (`tpodata_id`);

--
-- Indexes for table `tpowork_order`
--
ALTER TABLE `tpowork_order`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tpo_failure_state`
--
ALTER TABLE `tpo_failure_state`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `constant_config`
--
ALTER TABLE `constant_config`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `list_node`
--
ALTER TABLE `list_node`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tpodata`
--
ALTER TABLE `tpodata`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tpowork_order`
--
ALTER TABLE `tpowork_order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tpo_failure_state`
--
ALTER TABLE `tpo_failure_state`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `list_node`
--
ALTER TABLE `list_node`
  ADD CONSTRAINT `FKg2se4yakc6f3s6sl8h6njtbal` FOREIGN KEY (`next_node_id`) REFERENCES `list_node` (`id`);

--
-- Constraints for table `tpodata`
--
ALTER TABLE `tpodata`
  ADD CONSTRAINT `FK3jt471nxfofev66fblbmf21yb` FOREIGN KEY (`list_node_id`) REFERENCES `list_node` (`id`);

--
-- Constraints for table `tpodata_patterns`
--
ALTER TABLE `tpodata_patterns`
  ADD CONSTRAINT `FKj2sk3kvvaolcrev82xogci1k3` FOREIGN KEY (`patterns_id`) REFERENCES `tpowork_order` (`id`),
  ADD CONSTRAINT `FKr2wakx7dnvhmp8xqu1x1kn58n` FOREIGN KEY (`tpodata_id`) REFERENCES `tpodata` (`id`);

--
-- Constraints for table `tpodata_previous_states_data`
--
ALTER TABLE `tpodata_previous_states_data`
  ADD CONSTRAINT `FKan55cawys86yx88anc2hymi08` FOREIGN KEY (`previous_states_data_id`) REFERENCES `tpowork_order` (`id`),
  ADD CONSTRAINT `FKiy501r4679e5lh5bemx4qj4pe` FOREIGN KEY (`tpodata_id`) REFERENCES `tpodata` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
