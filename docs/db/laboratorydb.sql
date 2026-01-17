/*
 Navicat Premium Data Transfer

 Source Server         : zrrd
 Source Server Type    : MySQL
 Source Server Version : 80021 (8.0.21)
 Source Host           : localhost:3306
 Source Schema         : laboratorydb

 Target Server Type    : MySQL
 Target Server Version : 80021 (8.0.21)
 File Encoding         : 65001

 Date: 11/12/2025 09:13:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lab_booking
-- ----------------------------
DROP TABLE IF EXISTS `lab_booking`;
CREATE TABLE `lab_booking`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lab_id` int NOT NULL COMMENT '实验室id',
  `user_id` int NOT NULL COMMENT '预约用户id',
  `booking_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '预约日期',
  `time_slot` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '预约时段',
  `booking_num` int NOT NULL COMMENT '预约人数',
  `booking_status` tinyint(1) NULL DEFAULT 0 COMMENT '预约状态：0-待审核，1-已预约，2-未通过，3-已取消',
  `err_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预约失败原因',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '预约说明',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_booking_lab_id`(`lab_id` ASC) USING BTREE,
  INDEX `fk_lab_booking_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_booking_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_lab_booking_user_id` FOREIGN KEY (`user_id`) REFERENCES `lab_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室预约' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_booking
-- ----------------------------
INSERT INTO `lab_booking` VALUES (1, 1, 2, '2025-12-10', '上午', 1, 1, NULL, '', '爱吃油桃', '2025-12-10 11:03:49', '系统管理员', '2025-12-10 11:04:13', 0);

-- ----------------------------
-- Table structure for lab_course
-- ----------------------------
DROP TABLE IF EXISTS `lab_course`;
CREATE TABLE `lab_course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名',
  `teacher` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任课教师',
  `up_time` datetime NULL DEFAULT NULL COMMENT '上课时间',
  `down_time` datetime NULL DEFAULT NULL COMMENT '下课时间',
  `lab_id` int NULL DEFAULT NULL COMMENT '实验室ID',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_course_lab_id`(`lab_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_course_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室课程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_course
-- ----------------------------
INSERT INTO `lab_course` VALUES (1, 'Web前端框架', '马老师', '2025-12-11 08:30:00', '2025-12-11 10:05:00', 1, '系统管理员', '2025-12-10 11:02:22', '马老师', '2025-12-11 08:59:35', 0);

-- ----------------------------
-- Table structure for lab_device
-- ----------------------------
DROP TABLE IF EXISTS `lab_device`;
CREATE TABLE `lab_device`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备名称',
  `device_date` datetime NULL DEFAULT NULL COMMENT '购买时间',
  `device_price` int NULL DEFAULT NULL COMMENT '设备价格',
  `guarantee` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保修时间',
  `device_factory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产厂商',
  `device_status` tinyint(1) NULL DEFAULT 0 COMMENT '设备状态：0-正常，1-维修中，2-报废',
  `lab_id` int NULL DEFAULT NULL COMMENT '实验室ID',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_device_lab_id`(`lab_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_device_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_device
-- ----------------------------

-- ----------------------------
-- Table structure for lab_device_repair
-- ----------------------------
DROP TABLE IF EXISTS `lab_device_repair`;
CREATE TABLE `lab_device_repair`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` int NOT NULL COMMENT '设备ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '故障描述',
  `repair_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报修时间',
  `technician_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维修技术员',
  `status` tinyint(1) NULL DEFAULT 2 COMMENT '维修状态：1-已维修，2-未维修，3-维修中',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_device_repair_device_id`(`device_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_device_repair_device_id` FOREIGN KEY (`device_id`) REFERENCES `lab_device` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备报修信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_device_repair
-- ----------------------------

-- ----------------------------
-- Table structure for lab_info
-- ----------------------------
DROP TABLE IF EXISTS `lab_info`;
CREATE TABLE `lab_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验室名称',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_info
-- ----------------------------
INSERT INTO `lab_info` VALUES (1, '软件工程实验室', '系统管理员', '2025-12-10 10:56:44', '系统管理员', '2025-12-10 10:56:44', 0);

-- ----------------------------
-- Table structure for lab_login_log
-- ----------------------------
DROP TABLE IF EXISTS `lab_login_log`;
CREATE TABLE `lab_login_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地址',
  `driver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驱动名称',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `login_state` tinyint(1) NULL DEFAULT 0 COMMENT '登录状态：0-失败，1-成功',
  `login_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_login_log
-- ----------------------------
INSERT INTO `lab_login_log` VALUES (1, 'admin', '2025-12-10 10:56:19', '223.160.145.169', '中国|北京|北京市|广电', 'Quark null', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (2, 'youtao', '2025-12-10 10:58:59', '223.160.145.169', '中国|北京|北京市|广电', 'Quark null', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (3, 'admin', '2025-12-10 10:59:21', '223.160.145.169', '中国|北京|北京市|广电', 'MSEdge 143.0.0.0', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (4, 'admin', '2025-12-11 08:53:55', '223.160.145.169', '中国|北京|北京市|广电', 'Quark null', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (5, 'youtao', '2025-12-11 08:57:44', '223.160.145.169', '中国|北京|北京市|广电', 'Quark null', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (6, 'teacher', '2025-12-11 08:59:24', '223.160.145.169', '中国|北京|北京市|广电', 'MSEdge 143.0.0.0', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (7, 'admin', '2025-12-11 09:05:31', '223.160.145.169', '中国|北京|北京市|广电', 'Firefox 147.0', 'Windows 11', 1, '登录成功');
INSERT INTO `lab_login_log` VALUES (8, 'admin', '2025-12-11 09:13:01', '223.160.145.169', '中国|北京|北京市|广电', 'Firefox 147.0', 'Windows 11', 1, '登录成功');

-- ----------------------------
-- Table structure for lab_open
-- ----------------------------
DROP TABLE IF EXISTS `lab_open`;
CREATE TABLE `lab_open`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '负责人',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `open_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开放周期',
  `open_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开放时间',
  `lab_id` int NOT NULL COMMENT '实验室id',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_open_lab_id`(`lab_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_open_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室开放信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_open
-- ----------------------------
INSERT INTO `lab_open` VALUES (1, '系统管理员', '保密', '18888888888', '1-5', '9:00-21:00', 1, '系统管理员', '2025-12-10 10:57:27', '系统管理员', '2025-12-10 10:58:11', 0);

-- ----------------------------
-- Table structure for lab_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `lab_operation_log`;
CREATE TABLE `lab_operation_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `operation_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作模块',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  `operation_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作ip',
  `operation_addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作地址',
  `operation_state` tinyint(1) NULL DEFAULT 0 COMMENT '操作状态：0-失败，1-成功',
  `operation_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_operation_log
-- ----------------------------
INSERT INTO `lab_operation_log` VALUES (1, '系统管理员', '修改', '实验室开放管理', '2025-12-10 10:58:10', '223.160.145.169', '中国|北京|北京市|广电', 1, 'Result(code=200, message=操作成功, data=null)');
INSERT INTO `lab_operation_log` VALUES (2, '系统管理员', '修改', '实验室开放管理', '2025-12-10 10:57:48', '223.160.145.169', '中国|北京|北京市|广电', 1, 'Result(code=200, message=操作成功, data={token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlSWQiOiIzIiwibmlja25hbWUiOiLniLHlkIPmsrnmoYMiLCJleHAiOjE3NjUzNjQzMzksInVzZXJJZCI6IjIiLCJ1c2VybmFtZSI6InlvdXRhbyJ9.7L0vexTdV2yxk1Rm6obo4D7mrkOoD09VmXDXUTEgh1o})');
INSERT INTO `lab_operation_log` VALUES (3, '系统管理员', '修改', '实验室预约管理', '2025-12-10 11:04:13', '223.160.145.169', '中国|北京|北京市|广电', 1, 'Result(code=200, message=操作成功, data=null)');
INSERT INTO `lab_operation_log` VALUES (4, '系统管理员', '修改', '用户管理', '2025-12-11 08:54:33', '223.160.145.169', '中国|北京|北京市|广电', 1, 'Result(code=200, message=操作成功, data=null)');
INSERT INTO `lab_operation_log` VALUES (5, '系统管理员', '导出', '实验室课程管理', '2025-12-11 09:06:03', '223.160.145.169', '中国|北京|北京市|广电', 1, 'Result(code=200, message=操作成功, data={token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlSWQiOiIxIiwibmlja25hbWUiOiLns7vnu5_nrqHnkIblkZgiLCJleHAiOjE3NjU0NDQzODAsInVzZXJJZCI6IjEiLCJ1c2VybmFtZSI6ImFkbWluIn0.cJWYQksStXMNwoCZ6vuoalwr8DNQKqaqaqliBdLqDyU})');

-- ----------------------------
-- Table structure for lab_person
-- ----------------------------
DROP TABLE IF EXISTS `lab_person`;
CREATE TABLE `lab_person`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `lab_id` int NULL DEFAULT NULL COMMENT '实验室id',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_person_lab_id`(`lab_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_person_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室人员信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_person
-- ----------------------------
INSERT INTO `lab_person` VALUES (1, '你的油桃', '男', 23, '主楼w0504', 1, '系统管理员', '2025-12-10 11:00:22', '系统管理员', '2025-12-10 11:00:28', 0);

-- ----------------------------
-- Table structure for lab_project
-- ----------------------------
DROP TABLE IF EXISTS `lab_project`;
CREATE TABLE `lab_project`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` int NOT NULL COMMENT '课程ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '项目启动时间',
  `stop_time` datetime NULL DEFAULT NULL COMMENT '项目结束时间',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '项目介绍',
  `person_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目成员',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_project_course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_project_course_id` FOREIGN KEY (`course_id`) REFERENCES `lab_course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室课程项目信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_project
-- ----------------------------

-- ----------------------------
-- Table structure for lab_sub
-- ----------------------------
DROP TABLE IF EXISTS `lab_sub`;
CREATE TABLE `lab_sub`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lab_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验室分室名称',
  `lab_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验室类型',
  `lab_create_year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验室创建年份',
  `lab_area` int NULL DEFAULT NULL COMMENT '实验室面积',
  `lab_status` tinyint(1) NULL DEFAULT 0 COMMENT '实验室状态：0-可预约，1-维护中，2-使用中，3-待检中',
  `lab_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验室地点',
  `lab_seat` int NULL DEFAULT NULL COMMENT '实验室座位数',
  `lab_major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验室使用专业',
  `lab_id` int NOT NULL COMMENT '实验室id',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_lab_sub_lab_id`(`lab_id` ASC) USING BTREE,
  CONSTRAINT `fk_lab_sub_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验室分室信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_sub
-- ----------------------------
INSERT INTO `lab_sub` VALUES (1, 'Web实验室', '计算机实验室', '2025', 100, 0, '主楼w0507', 50, '计算机科学与技术,软件工程,物联网工程,数据科学与大数据技术', 1, '系统管理员', '2025-12-10 10:57:27', '系统管理员', '2025-12-10 10:57:27', 0);

-- ----------------------------
-- Table structure for lab_user
-- ----------------------------
DROP TABLE IF EXISTS `lab_user`;
CREATE TABLE `lab_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `user_sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `user_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0-正常，1-停用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `role_id` int NOT NULL DEFAULT 3 COMMENT '角色id：1-管理员，2-教师，3-学生',
  `create_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_user
-- ----------------------------
INSERT INTO `lab_user` VALUES (1, 'admin', '系统管理员', 'e10adc3949ba59abbe56e057f20f883e', '男', 'http://localhost:8070/uploads/1765414470177522238.jpg', 'admin@example.com', 0, '系统管理员', 1, '系统管理员', '2025-12-10 10:55:46', '系统管理员', '2025-12-11 08:54:34', 0);
INSERT INTO `lab_user` VALUES (2, 'youtao', '爱吃油桃', 'e10adc3949ba59abbe56e057f20f883e', '1', NULL, 'taitanyunluo2@126.com', 0, NULL, 3, '', '2025-12-10 10:58:50', '', '2025-12-10 10:58:50', 0);
INSERT INTO `lab_user` VALUES (3, 'teacher', '马老师', 'e10adc3949ba59abbe56e057f20f883e', '1', NULL, 'taitanyunluo2@126.com', 0, NULL, 2, '', '2025-12-11 08:59:02', '', '2025-12-11 08:59:02', 0);

SET FOREIGN_KEY_CHECKS = 1;
