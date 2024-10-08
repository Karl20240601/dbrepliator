/*
 Navicat Premium Data Transfer

 Source Server         : cdh1
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : cdh1:3306
 Source Schema         : push_center_m0

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 23/02/2022 18:42:48
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message0
-- ----------------------------
DROP TABLE IF EXISTS `message0`;
CREATE TABLE `message0`
(
    `msg_id`      bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `body`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `create_date` datetime(0) NULL DEFAULT NULL,
    `delay_level` int(11) NULL DEFAULT NULL,
    `ext_field_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_ids`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message1
-- ----------------------------
DROP TABLE IF EXISTS `message1`;
CREATE TABLE `message1`
(
    `msg_id`      bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `body`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `create_date` datetime(0) NULL DEFAULT NULL,
    `delay_level` int(11) NULL DEFAULT NULL,
    `ext_field_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_ids`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message2
-- ----------------------------
DROP TABLE IF EXISTS `message2`;
CREATE TABLE `message2`
(
    `msg_id`      bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `body`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `create_date` datetime(0) NULL DEFAULT NULL,
    `delay_level` int(11) NULL DEFAULT NULL,
    `ext_field_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_ids`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message3
-- ----------------------------
DROP TABLE IF EXISTS `message3`;
CREATE TABLE `message3`
(
    `msg_id`      bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `body`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `create_date` datetime(0) NULL DEFAULT NULL,
    `delay_level` int(11) NULL DEFAULT NULL,
    `ext_field_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `ext_field_4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_ids`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msg_stream0
-- ----------------------------
DROP TABLE IF EXISTS `msg_stream0`;
CREATE TABLE `msg_stream0`
(
    `stream_id`   bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `msg_id`      bigint(20) NOT NULL,
    `read_status` int(11) NULL DEFAULT NULL,
    `read_time`   datetime(0) NULL DEFAULT NULL,
    `send_status` int(11) NULL DEFAULT NULL,
    `send_time`   datetime(0) NULL DEFAULT NULL,
    `status`      int(11) NULL DEFAULT NULL,
    `target_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    PRIMARY KEY (`stream_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msg_stream1
-- ----------------------------
DROP TABLE IF EXISTS `msg_stream1`;
CREATE TABLE `msg_stream1`
(
    `stream_id`   bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `msg_id`      bigint(20) NOT NULL,
    `read_status` int(11) NULL DEFAULT NULL,
    `read_time`   datetime(0) NULL DEFAULT NULL,
    `send_status` int(11) NULL DEFAULT NULL,
    `send_time`   datetime(0) NULL DEFAULT NULL,
    `status`      int(11) NULL DEFAULT NULL,
    `target_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    PRIMARY KEY (`stream_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msg_stream2
-- ----------------------------
DROP TABLE IF EXISTS `msg_stream2`;
CREATE TABLE `msg_stream2`
(
    `stream_id`   bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `msg_id`      bigint(20) NOT NULL,
    `read_status` int(11) NULL DEFAULT NULL,
    `read_time`   datetime(0) NULL DEFAULT NULL,
    `send_status` int(11) NULL DEFAULT NULL,
    `send_time`   datetime(0) NULL DEFAULT NULL,
    `status`      int(11) NULL DEFAULT NULL,
    `target_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    PRIMARY KEY (`stream_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for msg_stream3
-- ----------------------------
DROP TABLE IF EXISTS `msg_stream3`;
CREATE TABLE `msg_stream3`
(
    `stream_id`   bigint(20) NOT NULL,
    `app_key`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `msg_id`      bigint(20) NOT NULL,
    `read_status` int(11) NULL DEFAULT NULL,
    `read_time`   datetime(0) NULL DEFAULT NULL,
    `send_status` int(11) NULL DEFAULT NULL,
    `send_time`   datetime(0) NULL DEFAULT NULL,
    `status`      int(11) NULL DEFAULT NULL,
    `target_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `target_type` int(11) NULL DEFAULT NULL,
    PRIMARY KEY (`stream_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
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
    `password`     varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `sex`          int(11) NULL DEFAULT NULL,
    `type`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `update_time`  datetime(0) NULL DEFAULT NULL,
    `user_name`    varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
