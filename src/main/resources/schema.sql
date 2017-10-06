
CREATE TABLE IF NOT EXISTS `seats` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `number` int NOT NULL,
 `level` int NOT NULL,
 `reserved` boolean NOT NULL DEFAULT 0,
 PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `reservations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(200) NOT NULL,
  `seat_ids` varchar(200) NOT NULL,
  `reserved_by` varchar(200) NOT NULL
);
