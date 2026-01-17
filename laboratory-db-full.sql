-- 张宇鹏的实验室管理系统数据库初始化脚本
-- 数据库名称: laboratorydb

-- 创建数据库
CREATE DATABASE IF NOT EXISTS laboratorydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE laboratorydb;

-- ----------------------------
-- Table structure for lab_user (用户表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_user`;
CREATE TABLE `lab_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '账号',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `user_sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `user_img` varchar(255) DEFAULT NULL COMMENT '头像',
  `user_email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0-正常，1-停用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `role_id` int(1) NOT NULL DEFAULT '3' COMMENT '角色id：1-管理员，2-教师，3-学生',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户信息';

-- ----------------------------
-- Records of lab_user
-- ----------------------------
INSERT INTO `lab_user` VALUES (1, 'admin', '系统管理员', 'e10adc3949ba59abbe56e057f20f883e', '男', NULL, 'admin@example.com', 0, '系统管理员', 1, '系统管理员', NOW(), '系统管理员', NOW(), 0);

-- ----------------------------
-- Table structure for lab_info (实验室表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_info`;
CREATE TABLE `lab_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '实验室名称',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室信息';

-- ----------------------------
-- Table structure for lab_sub (实验室分室表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_sub`;
CREATE TABLE `lab_sub` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lab_name` varchar(100) NOT NULL COMMENT '实验室分室名称',
  `lab_type` varchar(50) NOT NULL COMMENT '实验室类型',
  `lab_create_year` varchar(20) DEFAULT NULL COMMENT '实验室创建年份',
  `lab_area` int(11) DEFAULT NULL COMMENT '实验室面积',
  `lab_status` tinyint(1) DEFAULT '0' COMMENT '实验室状态：0-可预约，1-维护中，2-使用中，3-待检中',
  `lab_addr` varchar(255) DEFAULT NULL COMMENT '实验室地点',
  `lab_seat` int(11) DEFAULT NULL COMMENT '实验室座位数',
  `lab_major` varchar(255) DEFAULT NULL COMMENT '实验室使用专业',
  `lab_id` int(11) NOT NULL COMMENT '实验室id',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_sub_lab_id` (`lab_id`),
  CONSTRAINT `fk_lab_sub_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室分室信息';

-- ----------------------------
-- Table structure for lab_open (实验室开放信息表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_open`;
CREATE TABLE `lab_open` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '负责人',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `open_date` varchar(50) DEFAULT NULL COMMENT '开放周期',
  `open_time` varchar(50) DEFAULT NULL COMMENT '开放时间',
  `lab_id` int(11) NOT NULL COMMENT '实验室id',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_open_lab_id` (`lab_id`),
  CONSTRAINT `fk_lab_open_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室开放信息';

-- ----------------------------
-- Table structure for lab_person (实验室人员表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_person`;
CREATE TABLE `lab_person` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `age` int(3) DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `lab_id` int(11) DEFAULT NULL COMMENT '实验室id',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_person_lab_id` (`lab_id`),
  CONSTRAINT `fk_lab_person_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室人员信息';

-- ----------------------------
-- Table structure for lab_course (实验室课程表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_course`;
CREATE TABLE `lab_course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '课程名',
  `teacher` varchar(50) NOT NULL COMMENT '任课教师',
  `up_time` datetime DEFAULT NULL COMMENT '上课时间',
  `down_time` datetime DEFAULT NULL COMMENT '下课时间',
  `lab_id` int(11) DEFAULT NULL COMMENT '实验室ID',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_course_lab_id` (`lab_id`),
  CONSTRAINT `fk_lab_course_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室课程';

-- ----------------------------
-- Table structure for lab_project (实验室课程项目表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_project`;
CREATE TABLE `lab_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` int(11) NOT NULL COMMENT '课程ID',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `start_time` datetime DEFAULT NULL COMMENT '项目启动时间',
  `stop_time` datetime DEFAULT NULL COMMENT '项目结束时间',
  `description` text COMMENT '项目介绍',
  `person_ids` varchar(255) DEFAULT NULL COMMENT '项目成员',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_project_course_id` (`course_id`),
  CONSTRAINT `fk_lab_project_course_id` FOREIGN KEY (`course_id`) REFERENCES `lab_course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室课程项目信息';

-- ----------------------------
-- Table structure for lab_device (实验室设备表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_device`;
CREATE TABLE `lab_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_name` varchar(100) NOT NULL COMMENT '设备名称',
  `device_date` datetime DEFAULT NULL COMMENT '购买时间',
  `device_price` int(11) DEFAULT NULL COMMENT '设备价格',
  `guarantee` varchar(50) DEFAULT NULL COMMENT '保修时间',
  `device_factory` varchar(100) DEFAULT NULL COMMENT '生产厂商',
  `device_status` tinyint(1) DEFAULT '0' COMMENT '设备状态：0-正常，1-维修中，2-报废',
  `lab_id` int(11) DEFAULT NULL COMMENT '实验室ID',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_device_lab_id` (`lab_id`),
  CONSTRAINT `fk_lab_device_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室设备';

-- ----------------------------
-- Table structure for lab_device_repair (设备报修表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_device_repair`;
CREATE TABLE `lab_device_repair` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` int(11) NOT NULL COMMENT '设备ID',
  `description` text NOT NULL COMMENT '故障描述',
  `repair_date` varchar(50) NOT NULL COMMENT '报修时间',
  `technician_name` varchar(50) DEFAULT NULL COMMENT '维修技术员',
  `status` tinyint(1) DEFAULT '2' COMMENT '维修状态：1-已维修，2-未维修，3-维修中',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_device_repair_device_id` (`device_id`),
  CONSTRAINT `fk_lab_device_repair_device_id` FOREIGN KEY (`device_id`) REFERENCES `lab_device` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设备报修信息';

-- ----------------------------
-- Table structure for lab_booking (实验室预约表)
-- ----------------------------
DROP TABLE IF EXISTS `lab_booking`;
CREATE TABLE `lab_booking` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lab_id` int(11) NOT NULL COMMENT '实验室id',
  `user_id` int(11) NOT NULL COMMENT '预约用户id',
  `booking_date` varchar(50) NOT NULL COMMENT '预约日期',
  `time_slot` varchar(50) NOT NULL COMMENT '预约时段',
  `booking_num` int(11) NOT NULL COMMENT '预约人数',
  `booking_status` tinyint(1) DEFAULT '0' COMMENT '预约状态：0-待审核，1-已预约，2-未通过，3-已取消',
  `err_reason` varchar(255) DEFAULT NULL COMMENT '预约失败原因',
  `description` text COMMENT '预约说明',
  `create_at` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_lab_booking_lab_id` (`lab_id`),
  KEY `fk_lab_booking_user_id` (`user_id`),
  CONSTRAINT `fk_lab_booking_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `lab_sub` (`id`),
  CONSTRAINT `fk_lab_booking_user_id` FOREIGN KEY (`user_id`) REFERENCES `lab_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='实验室预约';

-- ----------------------------
-- Table structure for login_log (登录日志表)
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '登录用户',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录ip',
  `login_addr` varchar(100) DEFAULT NULL COMMENT '登录地址',
  `driver_name` varchar(100) DEFAULT NULL COMMENT '驱动名称',
  `os_name` varchar(100) DEFAULT NULL COMMENT '操作系统',
  `login_state` tinyint(1) DEFAULT '0' COMMENT '登录状态：0-失败，1-成功',
  `login_result` varchar(255) DEFAULT NULL COMMENT '登录结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='登录日志';

-- ----------------------------
-- Table structure for operation_log (操作日志表)
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '操作用户',
  `operation` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_module` varchar(50) NOT NULL COMMENT '操作模块',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  `operation_ip` varchar(50) DEFAULT NULL COMMENT '操作ip',
  `operation_addr` varchar(100) DEFAULT NULL COMMENT '操作地址',
  `operation_state` tinyint(1) DEFAULT '0' COMMENT '操作状态：0-失败，1-成功',
  `operation_result` text COMMENT '操作结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='操作日志';