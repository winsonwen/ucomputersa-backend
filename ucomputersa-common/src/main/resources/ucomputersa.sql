-- DROP TABLE IF EXISTS `user`;
-- DROP TABLE IF EXISTS `driver_ride_history`;
--
-- CREATE TABLE `computer_service_order`
-- (
--     `service_order_id` int NOT NULL AUTO_INCREMENT,
--     `order_status`     enum(255) NOT NULL,
--     `returning_date`  date         NOT NULL,
--     `user_id`         int          NOT NULL,
--     `departing_miles` int          DEFAULT NULL,
--     `returning_miles` int          DEFAULT NULL,
--     `ride_reason`     varchar(255) DEFAULT NULL,
--     PRIMARY KEY (`departing_date`, `driver_name`, `returning_date`, `user_id`)
-- );
--
--
--
-- CREATE TABLE `user`
-- (
--     `id`       int NOT NULL AUTO_INCREMENT,
--     `password` varchar(255) DEFAULT NULL,
--     `username` varchar(255) DEFAULT NULL,
--     PRIMARY KEY (`id`)
-- );
