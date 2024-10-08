*
db push_center_m
 drop
database   push_center_m0;
 drop
database   push_center_m1

 create
database push_center_m0 default character set utf8mb4 collate utf8mb4_unicode_ci;
 create
database push_center_m1 default character set utf8mb4 collate utf8mb4_unicode_ci;


TRUNCATE TABLE msg_stream0;
TRUNCATE TABLE msg_stream1;
TRUNCATE TABLE msg_stream2;
TRUNCATE TABLE msg_stream3;
TRUNCATE TABLE message3;
TRUNCATE TABLE message2;
TRUNCATE TABLE message1;
TRUNCATE TABLE message0;
TRUNCATE TABLE user_info;


ALTER TABLE `user_info` ENGINE = INNODB;
ALTER TABLE `message0` ENGINE = INNODB;
ALTER TABLE `message1` ENGINE = INNODB;
ALTER TABLE `message2` ENGINE = INNODB;
ALTER TABLE `message3` ENGINE = INNODB;
ALTER TABLE `msg_stream0` ENGINE = INNODB;
ALTER TABLE `msg_stream1` ENGINE = INNODB;
ALTER TABLE `msg_stream2` ENGINE = INNODB;
ALTER TABLE `msg_stream3` ENGINE = INNODB;

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`
(
    `user_id`      bigint(20) NOT NULL,
    `create_time`  datetime(0) NULL DEFAULT NULL,
    `enabled`      bit(1) NULL DEFAULT NULL,
    `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `is_del`       bit(1) NULL DEFAULT NULL,
    `mobile`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `nick_name`    varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `open_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `password`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `sex`          int(11) NULL DEFAULT NULL,
    `type`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `update_time`  datetime(0) NULL DEFAULT NULL,
    `user_name`    varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info`
VALUES (1, NULL, NULL, NULL, NULL, NULL, 'test', NULL, '$2a$10$AsCxXPI8B/JDzKK56ZACjuH9Pi2TuT6LLC0Nwh8Qt3a2eFp04gziy',
        NULL, NULL, NULL, 'test');
INSERT INTO `user_info`
VALUES (2, NULL, NULL, NULL, NULL, NULL, 'test1', NULL, '$10$AsCxXPI8B/JDzKK56ZACjuH9Pi2TuT6LLC0Nwh8Qt3a2eFp04gziy',
        NULL, NULL, NULL, 'test1');
