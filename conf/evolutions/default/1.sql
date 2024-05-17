# --- !Ups
CREATE TABLE `schedules` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `start_date` DATETIME NOT NULL,
  `end_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);

# --- !Downs
DROP TABLE `schedules`;
