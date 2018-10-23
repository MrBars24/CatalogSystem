CREATE DATABASE IF NOT EXISTS db_catalog;
USE db_catalog;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


CREATE TABLE `keywords` (
  `id` int(11) UNSIGNED NOT NULL,
  `research_id` int(11) UNSIGNED DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `researches` (
  `id` int(11) UNSIGNED NOT NULL,
  `title` varchar(500) NOT NULL,
  `author` varchar(500) DEFAULT NULL,
  `description` text,
  `publish_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `transaction` (
  `id` int(11) NOT NULL,
  `research_id` int(11) UNSIGNED DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `transaction_type` int(3) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `users` (`id`, `name`, `username`, `password`) VALUES
(1, 'Test', 'test', 'test');


ALTER TABLE `keywords`
  ADD PRIMARY KEY (`id`),
  ADD KEY `research_id` (`research_id`);


ALTER TABLE `researches`
  ADD PRIMARY KEY (`id`);


ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `research_id` (`research_id`);


ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `keywords`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;


ALTER TABLE `researches`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;


ALTER TABLE `transaction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;


ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;


ALTER TABLE `keywords`
  ADD CONSTRAINT `keywords_ibfk_1` FOREIGN KEY (`research_id`) REFERENCES `researches` (`id`);


ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`research_id`) REFERENCES `researches` (`id`);
COMMIT;